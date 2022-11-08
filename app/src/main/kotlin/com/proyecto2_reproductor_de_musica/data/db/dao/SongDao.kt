package com.proyecto2_reproductor_de_musica.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.relationships.withSongs.AlbumWithSongs


/**
 * Contains the methods used for accessing the database.
 */
@Dao
interface SongDao {

    //Todo: searches "search?:fields:keys"


    //:genre:pop  :person:shakira
    //join of pop songs with the author shakira


    @Query("SELECT * FROM rolas_table ORDER BY id_rola DESC")
     fun getAllSongs():LiveData<List<SongEntity>>





    //Todo: refactor the handle conflict
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAll(songsToInsert:List<SongEntity> )

    /**
     *pass an album name and get all songs in that album
     */
    @Transaction
    @Query("SELECT * FROM rolas_table WHERE id_album = :id ")
    suspend fun getAlbumWithSongs(id : Int):List<AlbumWithSongs>




}