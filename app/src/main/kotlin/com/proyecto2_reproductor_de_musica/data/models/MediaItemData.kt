package com.proyecto2_reproductor_de_musica.data.models

import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity


/**
 * Data class to encapsulate properties of a Media Item which are the songs
 */
data class MediaItemData(
    val mediaId: Int,
    val title: String,
    val author: String,
    val path: String,
    //val albumArtUri: Uri,
    //val browsable: Boolean,
   // var playbackRes: Int
)

/**
 * Maps SongEntity to MediaItemData
 */
fun SongEntity.toDomain() = MediaItemData(mediaId = id,title = title, author = performer , path = path)
//{
//
//    companion object {
//        /**
//         * Indicates [playbackRes] has changed.
//         */
//        const val PLAYBACK_RES_CHANGED = 1
//
//        val diffCallback = object : DiffUtil.ItemCallback<MediaItemData>() {
//            override fun areItemsTheSame(
//                oldItem: MediaItemData,
//                newItem: MediaItemData
//            ): Boolean =
//                oldItem.mediaId == newItem.mediaId
//
//            override fun areContentsTheSame(oldItem: MediaItemData, newItem: MediaItemData) =
//                oldItem.mediaId == newItem.mediaId && oldItem.playbackRes == newItem.playbackRes
//
//            override fun getChangePayload(oldItem: MediaItemData, newItem: MediaItemData) =
//                if (oldItem.playbackRes != newItem.playbackRes) {
//                    PLAYBACK_RES_CHANGED
//                } else null
//        }
//    }
//}

