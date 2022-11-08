package com.proyecto2_reproductor_de_musica.data.db.entities.relationships.withSongs

import androidx.room.Embedded
import androidx.room.Relation
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity

/**
 * One to N relationship between albums and songs
 */
data class AlbumWithSongs(
    @Embedded val album : AlbumsEntity,
    @Relation(
        parentColumn = "id_album",
        entityColumn = "id_album"
    )
    val songs: List<SongEntity>
)