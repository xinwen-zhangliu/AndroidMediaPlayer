package com.proyecto2_reproductor_de_musica.data

class MineWorker {

// Need the READ_EXTERNAL_STORAGE permission if accessing video files that your
// app didn't create.


//    val videoList = mutableListOf<MediaStore.Video>()
//
//    val collection =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            MediaStore.Video.Media.getContentUri(
//                MediaStore.VOLUME_EXTERNAL
//            )
//        } else {
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//        }
//
//    val projection = arrayOf(
//        MediaStore.Video.Media._ID,
//        MediaStore.Video.Media.DISPLAY_NAME,
//        MediaStore.Video.Media.DURATION,
//        MediaStore.Video.Media.SIZE
//    )
//
//    // Show only videos that are at least 5 minutes in duration.
//    val selection = "${MediaStore.Audio.Media.CONTENT_TYPE} >= ?"
//    val selectionArgs = arrayOf(
//        TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
//    )
//
//    // Display videos in alphabetical order based on their display name.
//    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
//
//    val query = ContentResolver.query(
//        collection,
//        projection,
//        selection,
//        selectionArgs,
//        sortOrder
//    )
//    query?.use { cursor ->
//        // Cache column indices.
//        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
//        val nameColumn =
//            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
//        val durationColumn =
//            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
//        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
//
//        while (cursor.moveToNext()) {
//            // Get values of columns for a given video.
//            val id = cursor.getLong(idColumn)
//            val name = cursor.getString(nameColumn)
//            val duration = cursor.getInt(durationColumn)
//            val size = cursor.getInt(sizeColumn)
//
//            val contentUri: Uri = ContentUris.withAppendedId(
//                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
//                id
//            )
//
//            // Stores column values and the contentUri in a local object
//            // that represents the media file.
//            videoList += Video(contentUri, name, duration, size)
//        }
//    }

}