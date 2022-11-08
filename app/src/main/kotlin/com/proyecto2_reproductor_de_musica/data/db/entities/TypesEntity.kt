package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "types_table")
//Todo: add rest of columns to table
data class TypesEntity (
    @PrimaryKey(autoGenerate = false) val id_type : Int,
    val description : String
)