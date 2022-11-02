package com.proyecto2_reproductor_de_musica.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.models.MediaItemData


class MediaItemDataViewHolder(view :View):RecyclerView.ViewHolder(view){
    val songTitle = view.findViewById<TextView>(R.id.title)
    val author = view.findViewById<TextView>(R.id.text)
    val image = view.findViewById<ImageView>(R.id.image)


    fun render(media : MediaItemData){
        songTitle.text = media.title
        author.text = media.subtitle

    }
}