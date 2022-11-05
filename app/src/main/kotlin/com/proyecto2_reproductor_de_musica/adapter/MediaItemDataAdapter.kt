package com.proyecto2_reproductor_de_musica.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.models.MediaItemData
import com.proyecto2_reproductor_de_musica.fragments.playing.PlayingFragment

/**
 * Adapter for displaying the items in the Recycler View
 */
class MediaItemDataAdapter (private var mediaList : List<MediaItemData>) : RecyclerView.Adapter<MediaItemDataViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemDataViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context )

        return MediaItemDataViewHolder(layoutInflater.inflate(R.layout.item_list_big, parent, false))

    }

    override fun onBindViewHolder(
        holder: MediaItemDataViewHolder,
        position: Int
    ) {
        val item = mediaList[position]
        holder.render(item)


        var title = holder.songTitle.toString()
        var subtitle = holder.author.toString()

        holder.itemView.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {

                    val activity = v!!.context as AppCompatActivity
                    val playingFragment =  PlayingFragment.newInstance(title , subtitle)
                    activity.supportFragmentManager.beginTransaction().replace(R.id.mainActivity, playingFragment).addToBackStack(null).commit()


            }
        })
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }
    fun setNewData(list : List<MediaItemData>){
        mediaList = list
        notifyDataSetChanged()

    }



}