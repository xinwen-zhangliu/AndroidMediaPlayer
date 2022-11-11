package com.proyecto2_reproductor_de_musica.fragments.list

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.adapter.MediaItemDataAdapter
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaItemData
import com.proyecto2_reproductor_de_musica.data.models.toDomain
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import com.proyecto2_reproductor_de_musica.databinding.FragmentListBinding
import com.proyecto2_reproductor_de_musica.services.PlayingService
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {


    private lateinit var mediaViewModel : MediaViewModel
    lateinit var binding : FragmentListBinding

    private  var temporaryList :List<MediaItemData> = mutableListOf()

    private lateinit var adapter: MediaItemDataAdapter
    private var loaded:MutableLiveData<Boolean> = MutableLiveData()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startStopService()

        binding = FragmentListBinding.inflate(inflater, container, false)
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)

        binding.progressBar.visibility = View.VISIBLE

        //var view =inflater.inflate(R.layout.fragment_list, container, false)
        displayMedia(binding.root)



        return binding.root
    }

    private var activityResultLauncher: ActivityResultLauncher<Array<String>>
    init{
        this.activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) {result ->
            var allAreGranted = true
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if(allAreGranted) {
                displayMedia(binding.root)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
        displayMedia(binding.root)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    override fun onDestroy() {
        activity!!.stopService(Intent(this.context, PlayingService::class.java ))
        super.onDestroy()
    }

    fun displayMedia(view :View ){

        Toast.makeText(this.context, "Displaying media ", 1).show()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        var mapping = listOf<MediaItemData>()


        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)

        mediaViewModel.readAllData.observe(viewLifecycleOwner, Observer<List<SongEntity>> {songEntity ->



            //adapter = MediaItemDataAdapter(mapping)
            //if(mediaViewModel.readAllData.value.isNullOrEmpty()){
            if(mediaViewModel.readAllData.value.isNullOrEmpty()){
                mediaViewModel.getMediaFromFiles()
                Toast.makeText(this.context, " There is nothing in the database so we are reading from files", Toast.LENGTH_SHORT).show()
                mediaViewModel.finishedReading.observe(viewLifecycleOwner, Observer<Boolean>{   boolean->
                    if(boolean){
                        if(!mediaViewModel.readAllData.value.isNullOrEmpty()){
                            mapping =  mediaViewModel.generalDao.getAllSongs().value!!.map { it.toDomain() }
                            adapter = MediaItemDataAdapter(mapping)
                            recyclerView.adapter= adapter
                            adapter.setNewData(mapping)
                            binding.progressBar.visibility = View.GONE
                            displayMedia(binding.root)
                            //loaded=true
                            observeSearchView(mapping, adapter)

                        }


                    }

                })

            }else{
                mapping = songEntity.map { it.toDomain() }
                Toast.makeText(this.context, "Found data from Database", Toast.LENGTH_SHORT).show()
                adapter = MediaItemDataAdapter(mapping)
                recyclerView.adapter= adapter
                //loaded=true
                observeSearchView(mapping,adapter)
                adapter.setNewData(mapping)
                binding.progressBar.visibility = View.GONE
            }





        })

    }

    fun observeSearchView(mapping : List<MediaItemData>, adapter: MediaItemDataAdapter){
        binding.listSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                val search = query!!.lowercase()
                if(search.isEmpty())
                    return false

                val fields : Array<String> = search.split(":").toTypedArray()
                if(!fields[0].equals("search?"))
                    return false

                //Fields:
                //song, artist, genre,

                var possibleFields : Array<String> =
                    arrayOf("song", "year", "artist", "genre", "type", "album" )


                for(i in 1..fields.size step 2){
                    var field = ""
                    if(possibleFields.contains(fields[i])){
                        var nextIndex = i +1
                        var toSearch = fields[nextIndex]
                        var rawQuery : String = "SELECT * FROM "

                    }


                }


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("x", "detected serch view input" + newText)
                val searchText = newText.toString().lowercase()
                if(searchText.endsWith("genre")){

                }

                var newList =  mapping.filter { mediaItemData ->  mediaItemData.title.lowercase().contains(searchText)}
                Log.d("x", "mapping new size: " + mapping.size)
                adapter.setNewData(newList)
                return true
            }

        })
    }


}