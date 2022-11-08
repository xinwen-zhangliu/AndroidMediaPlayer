package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "inGroup_table",
    primaryKeys = ["id_person", "id_group"],
    foreignKeys = [
        ForeignKey(entity = PersonsEntity::class,
        childColumns = ["id_person"],
        parentColumns = ["id_person"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = GroupsEntity::class,
        childColumns = ["id_group"],
        parentColumns = ["id_group"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE)
    ]
)
data class InGroupEntity(
    val id_person:Int, //Foreign key
    val id_group:Int //Foreign key
)