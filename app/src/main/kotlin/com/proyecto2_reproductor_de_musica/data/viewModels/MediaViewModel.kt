package com.proyecto2_reproductor_de_musica.data.viewModels

import android.app.Application
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.proyecto2_reproductor_de_musica.MusicPlayerApp
import com.proyecto2_reproductor_de_musica.data.db.MediaDatabase
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaDataProvider
import com.proyecto2_reproductor_de_musica.data.repositories.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

    /**
     * First executed when viewModel is called
     */
    init {
        val songDao = MediaDatabase.getDatabase(application).getSongDao()
        repository = MediaRepository(songDao)
        readAllData = repository.getAllMediaFromDatabase

    }

    fun addSongs(songs: List<SongEntity>){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertMedia(songs)
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
                                title,
                                author,
                                url
                            )
                        )

                    }while(cursor.moveToNext())
                cursor.close()
            }
        //}
        addSongs(MediaDataProvider.songsList)
    }
}
//Todo: read metadata frmo cursor instead of using cursor

//Todo: check if data base was created
//Todo: check if the info in database is correct
