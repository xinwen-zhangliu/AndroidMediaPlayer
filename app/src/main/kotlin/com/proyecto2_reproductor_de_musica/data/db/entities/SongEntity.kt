package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rolas_table")
//Todo: add rest of columns to table
data class SongEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id:Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "performer") val performer:String
)