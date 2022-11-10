package com.proyecto2_reproductor_de_musica.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(
        holder: MediaItemDataViewHolder,
        position: Int
    ) {
        val item = mediaList[position]
        holder.render(item)

        holder.itemView.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {

                //Todo: set audio as playing when entering the playing view
                //Todo: check if path is different than befores so we can start palying new song in service


                val title = holder.songTitle.toString()
                val subtitle = holder.author.toString()
                val path = holder.path
                Log.d("x", "item clicked" + title + path)

                    val activity = v!!.context as AppCompatActivity
                    val playingFragment =  PlayingFragment.newInstance(title , subtitle, path)
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