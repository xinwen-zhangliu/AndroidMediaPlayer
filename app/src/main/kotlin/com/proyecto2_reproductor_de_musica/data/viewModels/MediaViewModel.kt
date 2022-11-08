package com.proyecto2_reproductor_de_musica.data.viewModels

import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.proyecto2_reproductor_de_musica.MusicPlayerApp
import com.proyecto2_reproductor_de_musica.data.db.MediaDatabase
import com.proyecto2_reproductor_de_musica.data.db.dao.GeneralDao
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.TypesEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaDataProvider
import com.proyecto2_reproductor_de_musica.data.repositories.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val  mmr : MediaMetadataRetriever = MediaMetadataRetriever()

    /**
     * First executed when viewModel is called
     */
    init {
//        val songDao = MediaDatabase.getDatabase(application).getSongDao()
//        val typesDao = MediaDatabase.getDatabase(application).getTypeDao()
//        val albumsDao = MediaDatabase.getDatabase

        val generalDao = MediaDatabase.getDatabase(application).getGeneralDao()
        val rawdao = MediaDatabase.getDatabase(application).getRawDao()
        val relationsDao = MediaDatabase.getDatabase(application).getRelationsDao()
        repository = MediaRepository(generalDao, rawdao, relationsDao)

        readAllData = repository.getAllMediaFromDatabase

    }

    fun addSongs(songs: List<SongEntity>){
        viewModelScope.launch (Dispatchers.IO){

            repository.insertMedia(songs)
        }
    }

    fun createTypes(generalDao: GeneralDao){
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
        //viewModelScope.launch (Dispatchers.IO){
            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            val projection = arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DISPLAY_NAME,

                )

            var selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
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
                        var url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                        if(!url.isNullOrEmpty()){
                            try {
                                var file = File(url)
                                var uri= Uri.fromFile(file)
                                mmr.setDataSource(getApplication(), uri)
                                //get album
                                //get performer
                                //get song
                                //add it all to the databse in that order
                            }catch (e : Exception){

                            }
                        }
                        var author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                        var title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                        if(author==null){
                            author= "unknown"
                        }
                        if(title==null){
                            title =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                        }
                        MediaDataProvider.songsList.add(
                            SongEntity(
                                0,
                                0,
                                0,
                                url,
                                title,
                                0,
                                0,
                                "unknown"
                            )
                        )

                    }while(cursor.moveToNext())
                cursor.close()
            }
        //}
        //addSongs(MediaDataProvider.songsList)
    }
}

fun getSongEntity(mmr : MediaMetadataRetriever) : SongEntity{
    return SongEntity(0, 0, 0,"", "", 0, 0, "")
}

fun getAlbumEntity(mmr : MediaMetadataRetriever) : AlbumsEntity{
    return AlbumsEntity(0, "", "", 0)
}


fun getPerformerEntity(mmr : MediaMetadataRetriever) : PerformerEntity{
    return PerformerEntity(0,0,"")
}

//Todo: read metadata frmo cursor instead of using cursor

