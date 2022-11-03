package com.proyecto2_reproductor_de_musica.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.proyecto2_reproductor_de_musica.data.db.dao.SongDao
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity

@Database(entities = [SongEntity::class], version = 1)
abstract  class SongDatabase:RoomDatabase() {

    abstract fun getSongDao():SongDao


}