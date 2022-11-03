
package com.proyecto2_reproductor_de_musica.data

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import java.io.File


class MediaDataProvider (contextA: Context){

    private val context = contextA

    public var songsList = mutableListOf<MediaItemData>()


//    private fun checkingSongs(path :String ):ArrayList<String>{
//        var mineWorker = MineWorker()
//        val dir = Environment.getExternalStorageDirectory()
//        val path = dir.absolutePath
//        if (mineWorker.checking("/storage/sdcard0/Music") != null) {
//
//            val listOfFilePath: ArrayList<String> =
//                mineWorker.findSongs("/storage/sdcard0/Music")!! // here you will get all the files path which contains .mp3 at the end.
//            //do the remaining stuff
//            if(!listOfFilePath.isEmpty()){
//                for(pathName in listOfFilePath){
//
//                    var uri = Uri.fromFile(File(pathName))
//
//                    val mmr = MediaMetadataRetriever()
//
//                    try {
//                        mmr.setDataSource(context, uri)
//                    }catch (e : IllegalArgumentException){
//
//                        Log.e("MediaDataP.checkingSong", pathName)
//                        Log.e("MediaDataP.checkingSong", "illegal argument when setDAtaSource")
//
//                    }
//                    var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
//                    var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
//
//                    songsList.add(MediaItemData("abc",title,artist,true,0))
//                }
//            }
//            //Todo: check arrayList and display it ont he screen
//        } else {
//            Log.e("MediaDataP.checkingSong", "illegal argument when setDAtaSource")
//            //Toast.makeText(this, "sdCard not available", Toast.LENGTH_SHORT).show()
//        }
//    }
//    fun checkForSongs(rootPath: String?): ArrayList<MediaItemData>?{
//
//            return findSongs(rootPath)
//        }
//    }



    fun findSongs(rootPath: String?): ArrayList<MediaItemData>? {
        val fileList: ArrayList<MediaItemData> = ArrayList()

            return try {
                val rootFolder = File(rootPath)
                val files =
                    rootFolder.listFiles() //here you will get NPE if directory doesn't contains  any file,handle it like this.
                for (file in files) {
                    if (file.isDirectory) {
                        if (findSongs(file.absolutePath) != null) {
                            fileList.addAll(findSongs(file.absolutePath)!!)
                        } else {
                            break
                        }
                    } else if (file.name.endsWith(".mp3")) {
                        val mmr = MediaMetadataRetriever()
                        var uri = Uri.fromFile(File(file.absolutePath))
                        try {
                            mmr.setDataSource(context, uri)

                        } catch (e: IllegalArgumentException) {
                            Log.e("MediaDataP.checkingSong", "illegal argument when setDAtaSource")

                        }
                        var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                            .toString()
                        var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                            .toString()
                        Log.i("title", title)
                        Log.i("author", artist)
                        var newMedia = MediaItemData("id", title, artist)
                        fileList.add(newMedia)
                    }
                }
                fileList
            } catch (e: Exception) {
                Log.e("finds.MediaDataP", "exceptionsds")
                null
            }

    }

}