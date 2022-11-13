package com.proyecto2_reproductor_de_musica.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.fragments.list.PerformerListFragmentDirections

class PerformerItemAdapter (private var performersList : List<PerformerEntity>) : RecyclerView.Adapter<PerformerItemViewHolder>(){

        lateinit var parent: ViewGroup


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context, )
        this.parent = parent

        return PerformerItemViewHolder(layoutInflater.inflate(R.layout.item_performer_spinner, parent , false))
    }

    override fun onBindViewHolder(holder: PerformerItemViewHolder, position: Int) {
        val item = performersList[position]
        holder.render(item)

        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(holder.spinner.selectedItemPosition==0 ){
                    //Display the edit person info view
                    var action = PerformerListFragmentDirections.actionPerformerListFragmentToPersonInfoFragment(item)
                    holder.itemView.findNavController().navigate(action)
                }else if(holder.spinner.selectedItemPosition==1){
                    //Display the edit group info view
                    var action = PerformerListFragmentDirections.actionPerformerListFragmentToGroupInfoFragment(item)
                    holder.itemView.findNavController().navigate(action)

                }else{
                    Toast.makeText(parent.context, "You need to select a type to be able to edit", Toast.LENGTH_SHORT).show()
                }
            }

        })

    }

    override fun getItemCount(): Int {
        return performersList.size
    }
}
class  PerformerItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view){

    var  spinner = view.findViewById<Spinner>(R.id.type_spinner)

    public fun render(performer : PerformerEntity){
        spinner.setSelection(performer.id_type)

    }
}