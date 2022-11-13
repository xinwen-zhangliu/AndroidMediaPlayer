package com.proyecto2_reproductor_de_musica.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "persons_table",
    indices = [Index(value = ["stage_name"], unique = true)])
data class PersonsEntity(
    @PrimaryKey(autoGenerate = true) val id_person:Int,
    val stage_name:String,
    val real_name:String,
    val birth_date:String,
    val death_date:String
)