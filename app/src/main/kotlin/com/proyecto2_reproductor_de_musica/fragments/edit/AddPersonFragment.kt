package com.proyecto2_reproductor_de_musica.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PersonsEntity
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import com.proyecto2_reproductor_de_musica.databinding.FragmentGroupInfoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [AddPersonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPersonFragment : Fragment() {

    lateinit var  currentPerformer : PerformerEntity
    lateinit var binding: FragmentGroupInfoBinding
    private lateinit var mediaViewModel : MediaViewModel

    var isInDatabase = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val thisView = inflater.inflate(R.layout.fragment_add_person, container, false)
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)
        setData(thisView)
        val saveBtn = thisView.findViewById<FloatingActionButton>(R.id.savePersonInfo_btn)

        saveBtn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val stageName = thisView.findViewById<TextInputLayout>(R.id.stageName_layout)
                val realName = thisView.findViewById<TextInputLayout>(R.id.personRealName_layout)
                val birthDate = thisView.findViewById<TextInputLayout>(R.id.birthDate_layout)
                val deathDate = thisView.findViewById<TextInputLayout>(R.id.deathDate_layout)
                val person = PersonsEntity(
                    0,
                    checkUnknownString(stageName.editText?.text.toString()),
                    checkUnknownString(realName.editText?.text.toString()),
                    checkUnknownString(birthDate.editText?.text.toString()),
                    checkUnknownString(deathDate.editText?.text.toString())
                )

                val performer = PerformerEntity(
                    currentPerformer.id_performer,
                    currentPerformer.id_type,
                    checkUnknownString(stageName.editText?.text.toString()),
                )

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){

                    mediaViewModel.generalDao.insertPerson(person)

                    mediaViewModel.generalDao.updatePerformer(performer)
                }

                Toast.makeText(thisView.context, "Saved!", Toast.LENGTH_SHORT).show()
            }
        })
        return thisView
    }


    private fun setData(thisView : View){
        val stageName = thisView.findViewById<TextInputLayout>(R.id.stageName_layout)
        val realName = thisView.findViewById<TextInputLayout>(R.id.personRealName_layout)
        val birthDate = thisView.findViewById<TextInputLayout>(R.id.birthDate_layout)
        val deathDate = thisView.findViewById<TextInputLayout>(R.id.deathDate_layout)
        var person : PersonsEntity? = null
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            var result = mediaViewModel.generalDao.getPersonByName(currentPerformer.name)
            if(!result.isNullOrEmpty()){
                //the person is already in database
                person = result.first()
                isInDatabase = true
            }

            withContext(Dispatchers.Main){

                if(isInDatabase){

                    stageName.editText?.setText(person!!.stage_name)
                    realName.editText?.setText(person!!.real_name)
                    birthDate.editText?.setText(person!!.birth_date)
                    deathDate.editText?.setText(person!!.death_date)
                }else{
                    stageName.editText?.setText(currentPerformer.name)
                }
            }

        }



    }
    fun checkUnknownString(string : String) : String{
        if(string.isNullOrEmpty())
            return "unknown"
        return string
    }



}