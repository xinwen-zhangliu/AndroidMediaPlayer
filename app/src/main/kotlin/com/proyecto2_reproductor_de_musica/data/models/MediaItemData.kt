package com.proyecto2_reproductor_de_musica.data.models

import android.os.Parcelable
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import kotlinx.parcelize.Parcelize


/**
 * Data class to encapsulate properties of a Media Item which are the songs
 */
@Parcelize
data class MediaItemData(
    val mediaId: Int,
    val title: String,
    val author: Int,
    val path: String,
    val album : Int

    //val albumArtUri: Uri,
    //val browsable: Boolean,
   // var playbackRes: Int
):Parcelable

/**
 * Maps SongEntity to MediaItemData
 */
    fun SongEntity.toDomain() = MediaItemData(mediaId =id_rola,title = title, author = id_performer , path = path, album = id_album)

