package com.proyecto2_reproductor_de_musica.data.db.entities.relationships.withSongs

import androidx.room.Embedded
import androidx.room.Relation
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity

data class PerformerWithSongs(
    @Embedded val performer : PerformerEntity,
    @Relation(
        parentColumn = "id_performer",
        entityColumn = "id_performer"
    )
    val songs : List<SongEntity>
)