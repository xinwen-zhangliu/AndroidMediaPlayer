package com.proyecto2_reproductor_de_musica.data

import java.io.File


//Todo: figure out how to read all media files
class MineWorker {
    fun findSongs(rootPath: String?): ArrayList<String>? {
        //for sdCard use path /
        val fileList: ArrayList<String> = ArrayList()
        return try {
            val rootFolder = File(rootPath)
            val files: Array<File> = rootFolder.listFiles()
            //if directory does not contain any file it will return NPE, so we will check first
            for (file in files) {
                if (file.isDirectory) {
                    if (findSongs(file.absolutePath) != null) {
                        fileList.addAll(findSongs(file.absolutePath)!!)
                    } else {
                        break
                    }
                } else if (file.name.endsWith(".mp3")) {
                    fileList.add(file.absolutePath)
                }
            }
            fileList
        } catch (e: Exception) {
            null
        }
    }

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

