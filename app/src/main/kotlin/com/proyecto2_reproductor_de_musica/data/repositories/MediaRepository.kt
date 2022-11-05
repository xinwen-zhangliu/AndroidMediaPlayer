package com.proyecto2_reproductor_de_musica.data.repositories

import androidx.lifecycle.LiveData
import com.proyecto2_reproductor_de_musica.data.db.dao.SongDao
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity


//QuoteModel : From the dagger hilt and isign api?
//QuoteProvider : just a companion object to store the list of quotes
//QuoteService :


//Todo: check dependency injection
/**
 * For access to multiple data sources. Best practice for code separation and architecture
 */
class MediaRepository(
    private val songDao :SongDao
){
    val getAllMediaFromDatabase : LiveData<List<SongEntity>> = songDao.getAllSongs()

//    suspend fun getAllMediaFromDatabase(): LiveData<List<SongEntity>> {
//        val response = songDao.getAllSongs()
//        //return response.map{it.toDomain()}
//        return response
//    }

    /**
     * Function to insert new media into database.
     * This function will use corutines in the ViewModel
     */
    suspend fun insertMedia(media:List<SongEntity>){
        songDao.insertAll(media)
    }


}

