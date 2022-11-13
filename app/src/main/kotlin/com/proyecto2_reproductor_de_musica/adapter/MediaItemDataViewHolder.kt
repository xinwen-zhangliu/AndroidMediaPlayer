package com.proyecto2_reproductor_de_musica.adapter

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.models.MediaItemData
import com.proyecto2_reproductor_de_musica.fragments.list.ListFragmentDirections
import java.io.File


/**
 * Renders the view from MediaItemData instances
 */
class MediaItemDataViewHolder(view :View):RecyclerView.ViewHolder(view){
    val songTitle = view.findViewById<TextView>(R.id.title)
    val author = view.findViewById<TextView>(R.id.text)
    val image = view.findViewById<ImageView>(R.id.image)

    val moreOptions = view.findViewById<ImageButton>(R.id.moreInfoSong)

    var path = ""

    var view = view




    /**
     * Puts respective item data from MediaItemData into the elements in view
     */
    @RequiresApi(Build.VERSION_CODES.P)
    fun render(media : MediaItemData){

        moreOptions.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Log.d("x", "you clicked the more optiosn button on song " + media.title)


                var action = ListFragmentDirections.actionListFragmentToSongInfoFragment(media)
                itemView.findNavController().navigate(action)


            }
        })

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


    }





}