package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons_table")
data class PersonsEntity(
    @PrimaryKey(autoGenerate = true) val id_person:Int,
    val stage_name:String,
    val real_name:String,
    val birth_date:String,
    val death_date:String
)