package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "performer_table",
    foreignKeys = [ForeignKey(
        entity = TypesEntity::class,
        parentColumns = ["id_type"],
        childColumns = ["id_type"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class PerformerEntity(
    @PrimaryKey(autoGenerate = true) val id_performer:Int,
    val id_type:Int, //Foreign key
    val name: String,
)

