package com.proyecto2_reproductor_de_musica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity


@Dao
interface SongDao {
    @Query("SELECT * FROM rolas_table ORDER BY title DESC")
    suspend fun getAllSongs():List<SongEntity>

    //Todo: refactor the handle conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songsToInsert:List<SongEntity> )

}