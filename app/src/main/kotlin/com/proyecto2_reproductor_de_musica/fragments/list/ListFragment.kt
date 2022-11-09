package com.proyecto2_reproductor_de_musica.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.adapter.MediaItemDataAdapter
import com.proyecto2_reproductor_de_musica.data.models.MediaDataProvider
import com.proyecto2_reproductor_de_musica.data.models.toDomain
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel



/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {


    private lateinit var mediaViewModel : MediaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =inflater.inflate(R.layout.fragment_list, container, false)
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)
        if(mediaViewModel.readAllData.getValue().isNullOrEmpty()){
            mediaViewModel.getMediaFromFiles()
            //Todo: see if types get added to database.
            Toast.makeText(this.context, " There is nothing in the database so we are reading ", 1).show()
        }

        displayMedia(view)



        return view
    }

    fun displayMedia(view :View ){
        Toast.makeText(this.context, "Displaying media ", 1).show()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val newSongList = MediaDataProvider.songsList.map { it.toDomain() }
        var adapter = MediaItemDataAdapter(newSongList)
        recyclerView.adapter= adapter
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)
        mediaViewModel.readAllData.observe(viewLifecycleOwner, Observer { songEntity ->
            var mapping = songEntity.map { it.toDomain() }
            adapter.setNewData(mapping)
        })
    }



}