package com.proyecto2_reproductor_de_musica.adapter

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.models.MediaItemData
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.File


/**
 * Renders the view from MediaItemData instances
 */
class MediaItemDataViewHolder(view :View):RecyclerView.ViewHolder(view){
    val songTitle = view.findViewById<TextView>(R.id.title)
    val author = view.findViewById<TextView>(R.id.text)
    val image = view.findViewById<ImageView>(R.id.image)

    var path = ""

    var view = view




    /**
     * Puts respective item data from MediaItemData into the elements in view
     */
    @RequiresApi(Build.VERSION_CODES.P)
    @OptIn(DelicateCoroutinesApi::class)
    fun render(media : MediaItemData){


        songTitle.text = media.title


        this.path = media.path

        getLabels(media)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun getLabels(media :MediaItemData){
        val mmr = MediaMetadataRetriever()
        try {
            var file = File(media.path)
            mmr.setDataSource(view.context,Uri.fromFile(file))
        }catch (e : IllegalArgumentException){
            Log.d("x", "error in ViewHolder")
        }
        var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        var album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        songTitle.text = title
        var text = "$artist | $album"
        author.text = text
        //var hasImage = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_COUNT)?.toInt()?:-1
//        if(hasImage>0  ){
//            var imageAtIndex:Int =
//                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_PRIMARY)?.toInt() ?:-1
//            //if(imageAtIndex!=-1)
//                image.setImageBitmap(mmr.getImageAtIndex(imageAtIndex) )
//
//        }

        //val artBytes = mmr.embeddedPicture

//        if (artBytes != null) {
//            val bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
//            image.setImageBitmap(bm)
//        }
//        if(hasImage==-1){
//            image.setImageResource(R.drawable.image)
//        }

    }





}