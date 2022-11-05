package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents the songs table in the databse
 */
@Entity(tableName = "rolas_table")
//Todo: add rest of columns to table
data class SongEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "performer") val performer:String,
    @ColumnInfo(name = "path") val path: String,



    //Each entity corresponds to a table in the associated Room database

)
//fun MediaItemData.toDatabase() = SongEntity(title = title, performer = author, path = path)