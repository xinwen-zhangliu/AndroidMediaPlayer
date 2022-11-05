package com.proyecto2_reproductor_de_musica.data.models

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


}

