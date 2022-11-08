package com.proyecto2_reproductor_de_musica.data.db.entities.relationships.withPerformers

import androidx.room.Embedded
import androidx.room.Relation
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.TypesEntity

/**
 * One to N relationship between types and performers
 */
data class TypesWithPerformers(
    @Embedded val type: TypesEntity,
    @Relation(
        parentColumn = "id_type",
        entityColumn = "id_type"
    )
    val performers : List<PerformerEntity>
)