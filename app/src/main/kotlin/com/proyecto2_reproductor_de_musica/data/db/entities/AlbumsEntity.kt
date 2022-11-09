package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Table for albums
 */
@Entity(tableName = "albums_table",
    indices = [Index(value = ["name"], unique = true)])
data class AlbumsEntity(
    @PrimaryKey(autoGenerate = true) val id_album:Int,
    val path:String,
    val name:String,
    val year:Int,
)

