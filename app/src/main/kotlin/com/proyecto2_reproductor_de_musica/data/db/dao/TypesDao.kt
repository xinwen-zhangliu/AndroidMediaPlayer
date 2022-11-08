package com.proyecto2_reproductor_de_musica.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.TypesEntity

@Dao
interface TypesDao{
    @Insert
    suspend fun insert(types:
        List<TypesEntity>
    )

    @Query("SELECT * FROM types_table")
    fun getAllTypes(): List<SongEntity>
}