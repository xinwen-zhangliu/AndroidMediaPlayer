package com.proyecto2_reproductor_de_musica.data.db.entities.relationships.withIn_Group

import androidx.room.Embedded
import androidx.room.Relation
import com.proyecto2_reproductor_de_musica.data.db.entities.GroupsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity

/**
 * One to one relationship between Group and in_Group
 */
data class GroupsWithInGroups(
    @Embedded val group : GroupsEntity,
    @Relation(
        parentColumn = "id_",
        entityColumn = "album"
    )
    val songs: List<SongEntity>
)//Todo: check how to do one to one relationships and refactor