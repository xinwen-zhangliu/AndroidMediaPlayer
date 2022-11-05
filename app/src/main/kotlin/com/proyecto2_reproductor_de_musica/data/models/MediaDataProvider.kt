
package com.proyecto2_reproductor_de_musica.data.models

import android.content.Context
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity


class MediaDataProvider (context: Context){

    init {
        //readMediaFromStorage()
    }
    companion object{
        var songsList = mutableListOf<SongEntity>()
    }




//    val collection =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            MediaStore.Audio.Media.getContentUri(
//                MediaStore.VOLUME_EXTERNAL
//            )
//        } else {
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        }
//    val projection = arrayOf(
//        MediaStore.Audio.Media.DATA,
//        MediaStore.Audio.Media.ARTIST,
//        MediaStore.Audio.Media.DISPLAY_NAME,
//
//        )
//
//    var selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
//    var cursor = context.getContentResolver().query(
//        collection,
//        projection,
//        selection,
//        null,
//        null
//    )
//
//    fun readMediaFromStorage(){
//
//        if(cursor != null){
//            if(cursor!!.moveToFirst())
//                do{
//                    var url = cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
//                    var author = cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
//                    var title = cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
//                    if(author==null){
//                        author= "unknown"
//                    }
//                    if(title==null){
//                        title =  cursor!!.getString(cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
//                    }
//                    songsList.add(
//                        SongEntity(
//                            0,
//                            title,
//                            author,
//                            url
//                        )
//                    )
//
//                }while(cursor!!.moveToNext())
//            cursor!!.close()
//        }
//    }











}