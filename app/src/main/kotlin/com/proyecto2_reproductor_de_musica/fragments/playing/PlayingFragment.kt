package com.proyecto2_reproductor_de_musica.fragments.playing

import android.app.ActivityManager
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.PlayingLiveData
import com.proyecto2_reproductor_de_musica.databinding.FragmentPlayingBinding
import com.proyecto2_reproductor_de_musica.services.PlayingService
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "title"
//private const val ARG_PARAM2 = "subtitle"
//private const val ARG_PARAM3 = "time"
//private const val PATH_PARAM = "path"


/**
 * A simple [Fragment] subclass.
 * Use the [PlayingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayingFragment : Fragment(){
    // TODO: Rename and change types of parameters

    private var time : Int= 0
    //private var path: String? = null

    lateinit var pathOfSong :String




    companion object {

        @JvmStatic
        fun newInstance(title: String, subtitle: String, path :String) =
            PlayingFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, title)
//                    putString(ARG_PARAM2, subtitle)
//                    putInt(ARG_PARAM3, time)
                    //putString(PATH_PARAM, path)
                    pathOfSong = path


                    Log.d("x", "PlayingFragment received path" + path   )

                }
            }
    }


    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    lateinit var binding : FragmentPlayingBinding
    private var liveData = PlayingLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayingBinding.inflate(inflater, container, false)


        getLabels()



        if(!liveData.path.value.equals(pathOfSong)){
            liveData.setNewPath(pathOfSong!!)
        }


        binding.playBtn.setOnClickListener{
            togglePlayButton()
        }


        var file = File(pathOfSong)
        mp = MediaPlayer.create(this.context, Uri.fromFile(file))
        totalTime=  mp.duration
        binding.remainingTimeLabel.text = createTimeLabel(totalTime)


        // Position Bar
        binding.positionBar.max = totalTime
        binding.positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        //mp.seekTo(progress)
                        liveData.setNewPosition(progress)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )


        liveData.position.observe(viewLifecycleOwner, Observer<Int>{
            it?.let {

                //move seekbar to where it should be and change the numbers
                binding.positionBar.progress = liveData.position.value!!

                var elapsedTime = createTimeLabel(liveData.position.value!!)
                binding.elapsedTimeLabel.text = elapsedTime

//                var remainingTime = createTimeLabel(totalTime - liveData.position.value!!)
//                binding.remainingTimeLabel.text = "-$remainingTime"

            }
        })


        return binding.root
    }

    fun togglePlayButton(){
        if(liveData.state.value==true){
            //then service is playing
            liveData.setNewState(false)
            binding.playBtn.setBackgroundResource(R.drawable.stop)
        }else{
            liveData.setNewState(true)
            binding.playBtn.setBackgroundResource(R.drawable.play)
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
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




    fun getLabels( ){

        val mmr = MediaMetadataRetriever()
        try {
            Log.d("x", "getLabels file path: " + pathOfSong)
            var file = File(pathOfSong)
            mmr.setDataSource(binding.root.context, Uri.fromFile(file))
        }catch (e : IllegalArgumentException){
            Log.d("x", "IllegalArgumentException exception in Playing Fragment getLabels")
        }catch (e :Exception){
            Log.d("x", "Some other exception in Playing Fragment getLabels")
        }
        var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        var album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        binding.songTitle.text = title
        var text = "$artist|$album"
        binding.author.text = text
        var hasImage = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_COUNT)?.toInt()?:-1

//        if(hasImage==-1 || hasImage<1){
//            binding.songImage.setImageResource(R.drawable.image)
//            return
//        }
        val artBytes = mmr.embeddedPicture
        if (artBytes != null) {
            val bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
            binding.songImage.setImageBitmap(bm)
        }else{
            binding.songImage.setImageResource(R.drawable.image)
        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        playingViewModel.position.observe(viewLifecycleOwner, Observer<Int>{
//            it?.let {
//
//
//            }
//        })


    }


}
