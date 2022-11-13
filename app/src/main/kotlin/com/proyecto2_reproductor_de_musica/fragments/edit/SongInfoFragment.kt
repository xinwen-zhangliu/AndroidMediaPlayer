package com.proyecto2_reproductor_de_musica.fragments.edit


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.models.MediaItemData
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import com.proyecto2_reproductor_de_musica.databinding.FragmentSongInfoBinding
import kotlinx.coroutines.*


private const val MEDIA_ITEM = "media"

/**
 * A simple [Fragment] subclass.
 * Fragment to show the song, performer and album data
 * Can edit database
 */
class SongInfoFragment : Fragment() {

    private val args by navArgs<SongInfoFragmentArgs>()
    private lateinit var mediaViewModel : MediaViewModel
    lateinit var path: String
    lateinit var binding: FragmentSongInfoBinding
    //----------------------> Entities
    lateinit var songEntity: SongEntity
    lateinit var albumEntity: AlbumsEntity
    lateinit var performerEntity: PerformerEntity
    //---------------------->
    //lateinit var currentSong : MediaItemData

    companion object {
        @JvmStatic
        fun newInstance(media : MediaItemData) =
            SongInfoFragment().apply {
                arguments = Bundle().apply {
                    //currentSong = media
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

        Log.d("x", "displaying song info fragment")
        Log.d("x", "the of song is " )


        this.path = args.currentMediaItemData.path

        setMetadataInFields(view)

        var saveBtn = view.findViewById<FloatingActionButton>(R.id.save_btn)
        saveBtn.setOnClickListener{
            updateitem(view)
            Toast.makeText(binding.root.context, "Saved!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + job)

    fun setMetadataInFields(view : View){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){

            songEntity = mediaViewModel.generalDao.getSongById(args.currentMediaItemData.mediaId).first()
            albumEntity =  mediaViewModel.generalDao.getAlbumById(songEntity.id_album)!!.first()
            performerEntity = mediaViewModel.generalDao.getPerformerById(songEntity.id_performer)!!.first()


            Log.d("x", " result of album search " +mediaViewModel.generalDao.getAlbumById(songEntity.id_album) )
            Log.d("x", " result of performer search " + mediaViewModel.generalDao.getPerformerById(songEntity.id_performer).first())
//            if(mediaViewModel.generalDao.getSongById(args.currentMediaItemData.mediaId).isNullOrEmpty())
//                songEntity= mediaViewModel.generalDao.getSongById(args.currentMediaItemData.mediaId).first()
            Log.d("x", "song entity id " + songEntity.id_rola)
            Log.d("x", "album entity id " + songEntity.id_album)
            Log.d("x", "performer entity id " + songEntity.id_performer)

            Log.d("x", "song entity id " + songEntity.id_rola)
            Log.d("x", "album entity id " + albumEntity.id_album)
            Log.d("x", "performer entity id " + performerEntity.id_performer)
            withContext(Dispatchers.Main){
                Log.d("x", "adding data to fields")


                //------------song-----------

                var songTitle = view.findViewById<TextInputLayout>(R.id.songTitle_layout)
                songTitle.editText?.setText(songEntity.title)
                var songYear = view.findViewById<TextInputLayout>(R.id.year_layout)
                songYear.editText?.setText(songEntity.year.toString())
                var songTrack = view.findViewById<TextInputLayout>(R.id.trackNumber_layout)
                songTrack.editText?.setText(songEntity.track.toString())
                var songGenre = view.findViewById<TextInputLayout>(R.id.genre_layout)
                songGenre.editText?.setText(songEntity.genre)


                //---------performer-----------
                var performerName = view.findViewById<TextInputLayout>(R.id.performerName_layout)
                performerName.editText?.setText(performerEntity.name)
                var performerType = view.findViewById<Spinner>(R.id.performerType_spinner)
                performerType.setSelection(performerEntity.id_type)

                //----------album--------------
                var albumName = view.findViewById<TextInputLayout>(R.id.albumName_layout)
                albumName.editText?.setText(albumEntity.name)
                var albumYear = view.findViewById<TextInputLayout>(R.id.albumYear_layout)
                albumYear.editText?.setText(albumEntity.year.toString())
            }
        }
    }


    fun updateitem(view : View){
        //SONG DATA

        var songTitle = view.findViewById<TextInputLayout>(R.id.songTitle_layout)
        var songYear = view.findViewById<TextInputLayout>(R.id.year_layout)
        var year = 2022
            try {
                year = Integer.parseInt(songYear.editText?.text.toString())
            }catch (e : Exception){ }

        var songTrack = view.findViewById<TextInputLayout>(R.id.trackNumber_layout)
        var track = 0
        try {
            track = Integer.parseInt(songTrack.editText?.text.toString())
        }catch (e : Exception){ }
        var songGenre = view.findViewById<TextInputLayout>(R.id.genre_layout)


        //PERFORMER DATA
        var performerName = view.findViewById<TextInputLayout>(R.id.performerName_layout)
        var performerType = view.findViewById<Spinner>(R.id.performerType_spinner)
        //ALBUM DATA
        var albumName = view.findViewById<TextInputLayout>(R.id.albumName_layout)

        var albumYear = view.findViewById<TextInputLayout>(R.id.albumYear_layout)
        var yearAlbum = 2022
        try {
            yearAlbum = Integer.parseInt(songTrack.editText?.text.toString())
        }catch (e : Exception){ }



        //updated instances
        var song = SongEntity(
            songEntity.id_rola,
            songEntity.id_performer,
            songEntity.id_album,
            songEntity.path,
            checkUnknownString(songTitle.editText?.text.toString()),
            track,
            year,
            checkUnknownString(songGenre.editText?.text.toString()))


        val performer = PerformerEntity(
            performerEntity.id_performer,
            performerType.selectedItemPosition,
            checkUnknownString(performerName.editText?.text.toString())
        )



        var album = AlbumsEntity(
            albumEntity.id_album,
            albumEntity.path,
            checkUnknownString(albumName.editText?.text.toString()),
            yearAlbum
        )

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            mediaViewModel.generalDao.updateSong(song)
            mediaViewModel.generalDao.updatePerformer(performer)
            mediaViewModel.generalDao.updateAlbum(album)
        }



    }



    fun checkUnknownString(string : String) : String{
        if(string.isNullOrEmpty())
            return "unknown"
        return string
    }




}