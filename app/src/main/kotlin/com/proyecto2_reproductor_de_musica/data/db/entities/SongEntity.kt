package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.*


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
    @ColumnInfo(name = "id_performer") val id_performer:Int, //Foreign key
    @ColumnInfo(name = "id_album") val id_album:Int, //Foreign key
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "track") val track:Int,
    @ColumnInfo(name = "year") val year:Int,
    @ColumnInfo(name = "genre") val genre:String,
)

//fun MediaItemData.toDatabase() = SongEntity(title = title, performer = author, path = path)