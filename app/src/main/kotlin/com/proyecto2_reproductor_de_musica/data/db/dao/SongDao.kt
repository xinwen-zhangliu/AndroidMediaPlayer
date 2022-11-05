package com.proyecto2_reproductor_de_musica.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity


/**
 * Contains the methods used for accessing the database.
 */
@Dao
interface SongDao {

    //Todo: searches "search?:fields:keys"


    //:genre:pop  :person:shakira
    //join of pop songs with the author shakira


    @Query("SELECT * FROM rolas_table ORDER BY id DESC")
     fun getAllSongs():LiveData<List<SongEntity>>


    //Todo: refactor the handle conflict
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAll(songsToInsert:List<SongEntity> )




}