package com.proyecto2_reproductor_de_musica.data.viewModels

import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.proyecto2_reproductor_de_musica.MusicPlayerApp
import com.proyecto2_reproductor_de_musica.data.db.MediaDatabase
import com.proyecto2_reproductor_de_musica.data.db.dao.GeneralDao
import com.proyecto2_reproductor_de_musica.data.db.dao.RawDao
import com.proyecto2_reproductor_de_musica.data.db.dao.RelationsDao
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.TypesEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaDataProvider
import com.proyecto2_reproductor_de_musica.data.repositories.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


/**
 * Provides data to UI and survive configuration changes. A ViewModel acts as a communication
 * center between the Repository and the UI. In other words it's part of the MVVM architecture and acts as a controller
 * that connects UI and Model
 * Access all queries from Daos
 *
 */
class MediaViewModel(application : Application): AndroidViewModel(application) {
    val readAllData : LiveData<List<SongEntity>>

    private val repository : MediaRepository
    @RequiresApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private val  mmr : MediaMetadataRetriever = MediaMetadataRetriever()
     var generalDao : GeneralDao
     var rawDao: RawDao
     var relationsDao: RelationsDao

     var finishedReading : MutableLiveData<Boolean> = MutableLiveData()

    fun updateFinishedState(state : Boolean){
        finishedReading.value = state
    }

    /**
     * First executed when viewModel is called
     */
    init {
        generalDao = MediaDatabase.getDatabase(application).getGeneralDao()
        createTypes(generalDao)
        rawDao = MediaDatabase.getDatabase(application).getRawDao()
        relationsDao = MediaDatabase.getDatabase(application).getRelationsDao()
        repository = MediaRepository(generalDao, rawDao, relationsDao)

        readAllData = repository.getAllMediaFromDatabase

        finishedReading.value = false
    }


    fun addToDatabse(songs: List<SongEntity>, albums : List<AlbumsEntity>, performers : List<PerformerEntity>){
        viewModelScope.launch (Dispatchers.IO){
            //repository.insertPerformers(performers)
            //repository.insertAlbums(albums)
            repository.insertMedia(songs)

        }
    }

    fun createTypes(generalDao: GeneralDao){
        Toast.makeText(getApplication(), "Generating types", Toast.LENGTH_SHORT ).show()
        viewModelScope.launch (Dispatchers.IO){
            if(generalDao.getAllTypes().isNullOrEmpty()){
                generalDao.insertTypes(
                    listOf<TypesEntity>(
                        TypesEntity(0, "Person"),
                        TypesEntity(1, "Group"),
                        TypesEntity(2, "Unknown")
                    )
                )
            }
        }
    }

    fun getMediaFromFiles(){

        viewModelScope.launch (Dispatchers.IO){

            //Toast.makeText(getApplication(), "Getting Media From Files", Toast.LENGTH_SHORT ).show()
            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }


            val internalCollection = MediaStore.Audio.Media.INTERNAL_CONTENT_URI

           var collectionList =  listOf<Uri>(collection, internalCollection)


            val projection = arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DISPLAY_NAME,

                )

            var selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

            for(uri in collectionList){
                var cursor = getApplication<MusicPlayerApp>().contentResolver.query(
                    collection,
                    projection,
                    selection,
                    null,
                    null
                )


                if(cursor != null){
                    if(cursor.moveToFirst())
                        do{
                            //Toast.makeText(getApplication(), "Moving cursor,  ViewModel", Toast.LENGTH_SHORT ).show()

                            var url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                            if(!url.isNullOrEmpty()){

                                try {
                                    var file = File(url)
                                    var uri= Uri.fromFile(file)
                                    mmr.setDataSource(getApplication(), uri)
                                }catch (e : Exception){
                                    continue
                                }

                            }
                            var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
                            if(title.isEmpty())
                                title =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                            var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
                            if(artist.isEmpty())
                                artist = "unknown"
                            var track:Int = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)?.toInt()?: 0
                            var year : Int = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)?.toInt()?: 0
                            var genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE).toString()
                            if(genre.isEmpty())
                                genre = "unknown"
                            var albumPath = url.subSequence(0, url.lastIndexOf('/'))
                            var album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
                            if(album.isEmpty())
                                album = "unknown"



                            var performerEntity: PerformerEntity = PerformerEntity(0,2, artist)
                            var albumsEntity: AlbumsEntity = AlbumsEntity(0,albumPath.toString(),album, year)



                            generalDao.insertOneAlbum(albumsEntity)
                            generalDao.insertOnePerformer(performerEntity)


                            var albumId = generalDao.getAlbumFromName(album).first().id_album
                            var lastPerformerId =generalDao.getPerformerFromName(artist).first().id_performer
                            Log.d("x", "the lastid of album " +album + " : " + albumId)
                            Log.d("x", "the lastid of performer " +artist + " : " + lastPerformerId)



                            var songEntity: SongEntity = SongEntity(0, lastPerformerId, albumId, url, title, track, year, genre)
                            MediaDataProvider.songsList.add(songEntity)



                        }while(cursor.moveToNext())
                    cursor.close()
                    repository.insertMedia(MediaDataProvider.songsList)

                }

            }

            withContext(Dispatchers.Main){
                Log.d("x", "Finished reading files, setting livedata as true")
                finishedReading.value = true
            }
            //end of corrutine scope
        }


        //addToDatabse(MediaDataProvider.songsList, MediaDataProvider.albumList, MediaDataProvider.performerList)

    }
    fun toastStuff(text: String,number : Int){
        Toast.makeText(getApplication(), text + number, Toast.LENGTH_SHORT ).show()
    }
}

