package com.proyecto2_reproductor_de_musica.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.proyecto2_reproductor_de_musica.databinding.FragmentGroupInfoBinding

class GroupInfoFragment : Fragment() {

    private val args by navArgs<GroupInfoFragmentArgs>()
    lateinit var binding: FragmentGroupInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupInfoBinding.inflate(inflater, container, false)



        return binding.root
    }

    fun setData(){
        //val performer = args.currentPerformer
        //val groupName = binding.root.findViewById<TextInputLayout>(R.id)
    }

}