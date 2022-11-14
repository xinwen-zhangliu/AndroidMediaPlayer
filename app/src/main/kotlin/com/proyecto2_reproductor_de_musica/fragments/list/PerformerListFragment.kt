package com.proyecto2_reproductor_de_musica.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.adapter.PerformerItemAdapter
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import com.proyecto2_reproductor_de_musica.databinding.FragmentPerformerListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformerListFragment : Fragment() {


    private lateinit var mediaViewModel : MediaViewModel
    lateinit var performers : List<PerformerEntity>
    lateinit var binding: FragmentPerformerListBinding
    lateinit var adapter : PerformerItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerformerListBinding.inflate(inflater, container, false)
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)
        val view =  inflater.inflate(R.layout.fragment_performer_list, container, false)
        var foundPerformers = false
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            performers= mediaViewModel.generalDao.getAllPerformers()

            if(!performers.isNullOrEmpty()){
                foundPerformers= true

            }

            withContext(Dispatchers.Main){
                if(!foundPerformers){
                    showUserDialog("No performers found")
                }else{
                    showList(performers, view)
                    search(view, performers)
                }
            }
        }


        //return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    fun showList(list: List<PerformerEntity>, view : View){
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.performer_recyclerView)
        var layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        adapter  = PerformerItemAdapter(list)
        recyclerView.adapter= adapter
        var searchView = view.findViewById<SearchView>(R.id.performerList_searchView)

        binding.performerListSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("x", "performer list search : "+ newText)
                val searchText = newText.toString().lowercase()
                var newList =  list.filter { performerEntity ->  performerEntity.name.lowercase().contains(searchText)}
                adapter.setNewData(newList)
                return true

            }

        })




    }

    fun search(view : View, list: List<PerformerEntity> ){

    }


    private fun showUserDialog(message: String) {
        MaterialAlertDialogBuilder(this.context!!)
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ ->

            }
            .create().show()
//            .setNegativeButton("Cancel") { _, _->
//                Toast.makeText(this, "Ope", LENGTH_SHORT).show()
//            }
    }


}