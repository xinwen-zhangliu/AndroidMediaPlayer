package com.proyecto2_reproductor_de_musica.fragments.list

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteAbortException
import android.database.sqlite.SQLiteException
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.adapter.MediaItemDataAdapter
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaItemData
import com.proyecto2_reproductor_de_musica.data.models.toDomain
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import com.proyecto2_reproductor_de_musica.databinding.FragmentListBinding
import com.proyecto2_reproductor_de_musica.services.PlayingService
import kotlinx.coroutines.*
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
    private var result : MutableLiveData<List<SongEntity>> = MutableLiveData()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //startStopService()
        if(!isPlayingServiceRunning(PlayingService::class.java)){
            Toast.makeText(this.context, "service Started", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity!!.startForegroundService(Intent(this.context, PlayingService::class.java ))
            } else {
                activity!!.startService(Intent(this.context, PlayingService::class.java ))
            }
        }

        binding = FragmentListBinding.inflate(inflater, container, false)
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)

        binding.progressBar.visibility = View.VISIBLE

        //var view =inflater.inflate(R.layout.fragment_list, container, false)
        displayMedia(binding.root)

        //CLick listeners for the buttons
        binding.selectFolderBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d("x", "Select Folder button has been clicked")
                showdialog()

            }
        })

        binding.addPersonsBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                var action  = ListFragmentDirections.actionListFragmentToPerformerListFragment()
                binding.root.findNavController().navigate(action)
            }
        })

        return binding.root
    }
    fun showdialog() : String{
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this@ListFragment.context)
        builder.setTitle("Please write the full path to the folder")

        val input = EditText(this@ListFragment.context)

        input.setHint("Enter Path")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        var m_Text : String = ""
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

             m_Text = input.text.toString()
            mediaViewModel.getMediaFromFiles(m_Text)
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
        return m_Text
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
                mediaViewModel.getMediaFromFiles(null)
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
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    private fun showUserDialog(message: String) {
        MaterialAlertDialogBuilder(this@ListFragment.context!!)
            .setTitle("Invalid Search")
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                //onConfirmUserCreate(username)
            }
            .create().show()
//            .setNegativeButton("Cancel") { _, _->
//                Toast.makeText(this, "Ope", LENGTH_SHORT).show()
//            }
    }


    fun observeSearchView(mapping : List<MediaItemData>, adapter: MediaItemDataAdapter){
        binding.listSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("x", "onQueryTextSubmit: " + query)
                val search = query!!
                if (search.isEmpty())
                    return false
                val searchItems: Array<String> = search.split(":").toTypedArray()
                Log.d("x", searchItems.joinToString(","))
                if (!searchItems[0].equals("search?"))
                    return false
                var queryList = mutableListOf<String>()
                var newList = listOf<MediaItemData>()
                binding.progressBar.visibility = View.VISIBLE
                var message = ""
                var tables = "rolas_table"

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){

                    //Fields:

                    var possibleFields: Array<String> =
                        arrayOf("song", "year", "artist", "genre", "type", "album")


                    for (i in 1 until searchItems.size step 2) {
                        var field = ""
                        if (possibleFields.contains(searchItems[i])) {
                            var nextIndex = i + 1
                            var toSearch = searchItems[nextIndex]
                            Log.d("x", "field: " + searchItems[i] + " parameter: " + toSearch)

                            when (searchItems[i].trim()) {
                                "album" -> {
                                    Log.d("x", "inside album case with " + searchItems[i])
                                    var albumId = 0
                                    try{
                                        albumId =mediaViewModel.generalDao
                                            .getAlbumFromNameLowercase(toSearch.trim().lowercase())
                                            .first().id_album
                                    }catch (e : Exception){
                                        message = "No album with such name found"
                                    }

                                    Log.d("x", "album query result = " + albumId)

                                    queryList.add( "id_album = " + albumId  )


                                }
                                "song" -> {
                                    var result = 0
                                    try{
                                        //result = mediaViewModel.generalDao.getSongById().first().id_album
                                    }catch (e : Exception){

                                    }
                                    //var albumId = result.first().id_album
                                    //rawQuery += "id_album = " + albumId

                                }
                                "artist" -> {
                                    var performerId = 0
                                    try{
                                        performerId =mediaViewModel.generalDao
                                            .getPerformerFromNameLowercase(toSearch.trim().lowercase())
                                            .first().id_performer
                                    }catch (e : Exception){
                                        message = "No artist with such name found"
                                    }
                                    if(performerId==0)
                                        message+="\nArtist not found"
                                    queryList.add( "id_performer = " + performerId )
                                }
                                "genre" -> {
                                    var string = " genre like '%"+searchItems[nextIndex]+"%'"
                                    queryList.add(string)
                                    //rawQuery += " genre LIKE " + searchItems[nextIndex]
                                }
                                "type" -> {
                                    var type = 2
                                    when(searchItems[nextIndex].lowercase()){
                                        "unknown"->{
                                            type = 2
                                        }
                                        "person"->{
                                            type = 0
                                        }
                                        "group"->{
                                            type= 1
                                        }
                                        else->{
                                            message+="Invalid type"
                                        }
                                    }
                                    var string = " "
                                    queryList.add(string)
                                    //rawQuery += " type"
                                }
                                "year"->{

                                }
                                else -> {
                                    Toast.makeText(
                                        this@ListFragment.context,
                                        "Search query invalid, please try again",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }


                    }
                    var rawQuery : String= "SELECT * FROM rolas_table WHERE "+ queryList.joinToString(" AND ")
                    Log.d("x", "query to search: " + rawQuery)
                    var query = SimpleSQLiteQuery(
                        rawQuery

                    )



                    var rawQueryResult = listOf<SongEntity>()
                    try{
                        rawQueryResult = mediaViewModel.rawDao.getSongsViaQuery(query)
                    }catch (e : Exception){
                        Log.d("x", "SQLiteException")

                    }catch (e : SQLiteException){
                        Log.d("x", "SQLiteException")
                    }catch (e : SQLiteAbortException){

                    }catch (e : android.database.SQLException){

                    }catch (e : java.util.NoSuchElementException){
                        Log.d("x", "no suck element exception")
                    }

                    if (!rawQueryResult.isNullOrEmpty()) {
                        Log.d("x", "rawquery result  " + rawQueryResult)
                        Log.d("x", "results after mapping " + rawQueryResult.map { it.toDomain() })
                        newList= rawQueryResult.map { it.toDomain() }


                    } else {
                        Log.d("x", "rawquery result is null or empty " + rawQueryResult)

                    }

                    Log.d("x", "result of query")

                    withContext(Dispatchers.Main){
                        binding.progressBar.visibility = View.GONE
                        if(!newList.isEmpty()){
                            adapter.setNewData(newList)
                        }else{
                            Log.d("x", "newList is empty")
                            if(message.isEmpty())
                                message = "No results"
                            showUserDialog(message)
                        }
                    }

                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("x", "detected serch view input" + newText)
                val searchText = newText.toString().lowercase()


                var newList =  mapping.filter { mediaItemData ->  mediaItemData.title.lowercase().contains(searchText)}
                //Log.d("x", "mapping new size: " + mapping.size)
                adapter.setNewData(newList)
                return true
            }

        })
    }





}