package com.proyecto2_reproductor_de_musica.fragments.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaItemData
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import com.proyecto2_reproductor_de_musica.databinding.FragmentSongInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


private const val MEDIA_ITEM = "media"

/**
 * A simple [Fragment] subclass.
 * Fragment to show the song, performer and album data
 * Can edit database
 */
class SongInfoFragment : Fragment() {

    private lateinit var mediaViewModel : MediaViewModel
    lateinit var path: String
    lateinit var binding: FragmentSongInfoBinding
    lateinit var currentSong : MediaItemData

    companion object {
        @JvmStatic
        fun newInstance(media : MediaItemData) =
            SongInfoFragment().apply {
                arguments = Bundle().apply {
                    currentSong = media
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongInfoBinding.inflate(inflater, container, false)
        val view =  inflater.inflate(R.layout.fragment_song_info, container, false)
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)

        setMetadataInFields()

        binding.saveBtn.setOnClickListener{

        }

        return view
    }

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun setMetadataInFields(){
        uiScope.launch(Dispatchers.IO) {
            //mediaViewModel.generalDao.

        }
    }


    fun updateitem(songEntity: SongEntity, albumsEntity: AlbumsEntity, performerEntity: PerformerEntity){
        //SONG DATA
        var track : Int = 0
        if(!binding.trackNumber.text.isNullOrEmpty()) {
             track= Integer.parseInt(binding.trackNumber.text.toString())
        }
        Log.d("x", "track number")
        var title : String = binding.songTitle.text.toString()
        var genre : String = binding.genreEdtText.text.toString()
        var year : Int = 0
        if(!binding.year.text.isNullOrEmpty()) {
            year= Integer.parseInt(binding.year.text.toString())
        }


        //PERFORMER DATA


        //ALBUM DATA
        var albumName : String = binding.albumNameEditText.text.toString()



        //updated instances
        var song = SongEntity(
            songEntity.id_rola,
            songEntity.id_performer,
            songEntity.id_album,
            songEntity.path,
            checkUnknownString(title),
            track,
            year,
            checkUnknownString(genre))
//
//        var album = AlbumsEntity(
//            songEntity.id_album,
//            checkUnknownString(albumName)
//        )

    }



    fun checkUnknownString(string : String) : String{
        if(string.isNullOrEmpty())
            return "unknown"
        return string
    }



}