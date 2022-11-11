package com.proyecto2_reproductor_de_musica.fragments.playing

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.databinding.FragmentPlayingBinding
import com.proyecto2_reproductor_de_musica.services.PlayingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

                    pathOfSong = path


                    Log.d("x", "PlayingFragment received path" + path   )

                }
            }
    }


    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    lateinit var binding : FragmentPlayingBinding

    val fragmentJob = Job()
    val fragmentScope = CoroutineScope(Dispatchers.Main +fragmentJob)


    private lateinit var mService : PlayingService
    private var mBound : Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as PlayingService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }
//    @SuppressLint("HandlerLeak")
//    var handler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            var currentPosition = msg.what
//            var elapsedtime = createTimeLabel(currentPosition)
//            binding.elapsedTimeLabel.text = elapsedtime
//        }
//    }

    //lateinit var runnable : kotlinx.coroutines.Runnable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayingBinding.inflate(inflater, container, false)

        var file = File(pathOfSong)
        mp = MediaPlayer.create(this.context, Uri.fromFile(file))
        totalTime=  mp.duration
        binding.remainingTimeLabel.text = createTimeLabel(totalTime)

        //Binding to service
        var musicIntent = Intent(activity,PlayingService::class.java)
        musicIntent.putExtra("pathOfSong", pathOfSong)
        musicIntent.also {intent ->
            activity!!.bindService(musicIntent, connection, Context.BIND_AUTO_CREATE)
        }



//        runnable = Runnable {
//            if(mBound){
//                while (mService.getSongCurrPos()<= totalTime) {
//                    try {
//                        var msg = Message()
//                        msg.what = mService.getSongCurrPos()
//                        handler.sendMessage(msg)
//                        Thread.sleep(1000)
//                    } catch (e: InterruptedException) {
//                    }
//                }
//            }else{
//                handler.postDelayed(runnable, 1000)
//            }
//
//        }
//        var threadT = Thread(runnable).start()
//        handler.postDelayed(runnable, 1000)



        getLabels()



        binding.playBtn.setOnClickListener{
            togglePlayButton()
        }




        // Position Bar
        binding.positionBar.max = totalTime
        binding.positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                       mService.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

//        liveData.position.observe(viewLifecycleOwner, Observer<Int>{
//            it?.let {
//                fragmentScope.launch(Dispatchers.IO) {
//                    //move seekbar to where it should be and change the numbers
//                    binding.positionBar.progress = liveData.position.value!!
//
//                    var elapsedTime = createTimeLabel(liveData.position.value!!)
//                    binding.elapsedTimeLabel.text = elapsedTime
//
////                var remainingTime = createTimeLabel(totalTime - liveData.position.value!!)
////                binding.remainingTimeLabel.text = "-$remainingTime"
//                    withContext(Dispatchers.Main){
//
//                    }
//                }
//
//
//            }
//        })




        return binding.root
    }

    override fun onDestroy() {
        fragmentJob.cancel()
        super.onDestroy()

    }
    fun togglePlayButton(){
        if(mService.isPlaying()){
            binding.playBtn.setBackgroundResource(R.drawable.stop)
        }else{
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
