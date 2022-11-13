package com.proyecto2_reproductor_de_musica.data.db.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "performer_table",
    foreignKeys = [ForeignKey(
        entity = TypesEntity::class,
        parentColumns = ["id_type"],
        childColumns = ["id_type"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["name"], unique = true)]
)
data class PerformerEntity(
    @PrimaryKey(autoGenerate = true) val id_performer:Int,
    val id_type:Int, //Foreign key
    val name: String,
):Parcelable

