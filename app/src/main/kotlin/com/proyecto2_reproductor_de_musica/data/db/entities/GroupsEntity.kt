package com.proyecto2_reproductor_de_musica.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "groups_table",
    indices = [Index(value = ["name"], unique = true)],)
data class GroupsEntity(
    @PrimaryKey(autoGenerate = true) val id_group:Int,
    val name : String,
    val start_date:String,
    val end_date : String
)
