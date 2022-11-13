package com.proyecto2_reproductor_de_musica.data.repositories

import androidx.lifecycle.LiveData
import com.proyecto2_reproductor_de_musica.data.db.dao.GeneralDao
import com.proyecto2_reproductor_de_musica.data.db.dao.RawDao
import com.proyecto2_reproductor_de_musica.data.db.dao.RelationsDao
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity


/**
 * For access to multiple data sources. Best practice for code separation and architecture
 */
class MediaRepository(
    private val generalDao: GeneralDao,
    private val rawDao: RawDao,
    private val relationsDao: RelationsDao
){

    /**
     * Function to get all the songs in database, returns as livedata
     */
    val getAllMediaFromDatabase : LiveData<List<SongEntity>> = generalDao.getAllSongs()

    /**
     * Function to insert new media into database.
     * This function will use corutines in the ViewModel
     */
    suspend fun insertMedia(media:List<SongEntity>){
        if(media.isNotEmpty())
            generalDao.insertAllSongs(media)
    }

    suspend fun insertAlbums(albums: List<AlbumsEntity>){

        generalDao.insertAllAlbums(albums)
    }

    suspend fun insertPerformers(performers : List<PerformerEntity>){
        generalDao.insertAllPerformers(performers)
    }




}

