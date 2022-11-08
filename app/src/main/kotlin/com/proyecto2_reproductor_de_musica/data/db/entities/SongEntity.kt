package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Represents the songs table in the databse
 */
@Entity(indices = [Index(value = ["path"], unique = true)],
    tableName = "rolas_table",
    foreignKeys = [ForeignKey(
        entity = PerformerEntity::class,
        childColumns = ["id_performer"],
        parentColumns = ["id_performer"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ),ForeignKey(
        entity = AlbumsEntity::class,
        childColumns = ["id_album"],
        parentColumns = ["id_album"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class SongEntity (
    @PrimaryKey(autoGenerate = true) val id_rola:Int,
    val id_performer:Int, //Foreign key
    val id_album:Int, //Foreign key
    val path: String,
    val title:String,
    val track:Int,
    val year:Int,
    val genre:String,
)

//fun MediaItemData.toDatabase() = SongEntity(title = title, performer = author, path = path)