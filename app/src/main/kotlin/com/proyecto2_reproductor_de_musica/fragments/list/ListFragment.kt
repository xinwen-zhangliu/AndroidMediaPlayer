package com.proyecto2_reproductor_de_musica.fragments.list

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.proyecto2_reproductor_de_musica.data.PlayingLiveData
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaDataProvider
import com.proyecto2_reproductor_de_musica.data.models.toDomain
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import com.proyecto2_reproductor_de_musica.databinding.FragmentListBinding
import com.proyecto2_reproductor_de_musica.services.PlayingService


/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {


    private lateinit var mediaViewModel : MediaViewModel
    lateinit var binding : FragmentListBinding
    var liveData = PlayingLiveData()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startStopService()
        binding = FragmentListBinding.inflate(inflater, container, false)

        var view =inflater.inflate(R.layout.fragment_list, container, false)
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)


            displayMedia(binding.root)


        return binding.root
    }
    private fun startStopService(){

        if(isPlayingServiceRunning(PlayingService::class.java)){
            Toast.makeText(this.context, "Service Stopped", Toast.LENGTH_SHORT).show()
            activity!!.stopService(Intent(this.context, PlayingService::class.java))
        }else{
            Toast.makeText(this.context, "service Started", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity!!.startForegroundService(Intent(this.context, PlayingService::class.java ))
            } else {
                activity!!.startService(Intent(this.context, PlayingService::class.java ))
            }
            //activity!!.startForegroundService(Intent(this.context, PlayingService::class.java ))
        }
    }
    private fun isPlayingServiceRunning(mClass : Class<PlayingService>) : Boolean{
        val manager: ActivityManager = getActivity()!!.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager

        for(service : ActivityManager.RunningServiceInfo
        in manager.getRunningServices(Integer.MAX_VALUE)){
            if(mClass.name.equals(service.service.className)){
                return true
            }
        }
        return false
    }

    fun displayMedia(view :View ){

        Toast.makeText(this.context, "Displaying media ", 1).show()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val newSongList = MediaDataProvider.songsList.map { it.toDomain() }
        var adapter = MediaItemDataAdapter(newSongList)
        recyclerView.adapter= adapter
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)

        mediaViewModel.readAllData.observe(viewLifecycleOwner, Observer<List<SongEntity>> {songEntity ->
            var mapping = songEntity.map { it.toDomain() }
            if(mediaViewModel.readAllData.value.isNullOrEmpty()){
                mediaViewModel.getMediaFromFiles()
                Toast.makeText(this.context, " There is nothing in the database so we are reading from files", Toast.LENGTH_SHORT).show()
            }
            adapter.setNewData(mapping)
            binding.progressBar.visibility = View.GONE
        })

    }



}