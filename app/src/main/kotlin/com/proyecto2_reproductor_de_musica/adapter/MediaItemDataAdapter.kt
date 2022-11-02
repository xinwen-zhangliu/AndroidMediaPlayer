package com.proyecto2_reproductor_de_musica.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.models.MediaItemData

class MediaItemDataAdapter (private val mediaLits : List<MediaItemData>) : RecyclerView.Adapter<MediaItemDataViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemDataViewHolder {
        //TODO("Not yet implemented")
        val layoutInflater = LayoutInflater.from(parent.context )

        return MediaItemDataViewHolder(layoutInflater.inflate(R.layout.item_list_big, parent, false))

    }

    override fun onBindViewHolder(holder: MediaItemDataViewHolder, position: Int) {
        //TODO("Not yet implemented")
        val item = mediaLits[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        //TODO("Not yet implemented")
        return mediaLits.size
    }

}