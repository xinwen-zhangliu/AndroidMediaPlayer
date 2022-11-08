package com.proyecto2_reproductor_de_musica.data.db.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity

@Dao
interface RawDao {

    @RawQuery
    fun getSongsViaQuery(query: SupportSQLiteQuery): SongEntity
}

