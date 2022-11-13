package com.proyecto2_reproductor_de_musica.fragments.playing

import android.annotation.SuppressLint
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
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.databinding.FragmentPlayingBinding
import com.proyecto2_reproductor_de_musica.services.PlayingService
import kotlinx.coroutines.*
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

    private var currentPosition = 0

    val RECEIVER_INTENT = "RECEIVER_INTENT"
    val RECEIVER_MESSAGE = "RECEIVER_MESSAGE"

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
    private var mBound :MutableLiveData<Boolean> = MutableLiveData()

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as PlayingService.LocalBinder
            mService = binder.getService()
            mService.setPath(pathOfSong)
            mBound.value = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound.value = false
        }
    }


    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)



    private var paused : Boolean = false

    lateinit var runnable : kotlinx.coroutines.Runnable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayingBinding.inflate(inflater, container, false)


        var file = File(pathOfSong)
        mp = MediaPlayer.create(this.context, Uri.fromFile(file))
        totalTime=  mp.duration
        binding.remainingTimeLabel.text = createTimeLabel(totalTime)

        //Binding to service
        var musicIntent = Intent(activity,PlayingService::class.java)
        activity!!.intent.removeExtra("pathOfSong")
        musicIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        musicIntent = Intent(activity,PlayingService::class.java)
        musicIntent.putExtra("pathOfSong", pathOfSong)

        Log.d("x", "pathofsong in intent " + pathOfSong)
        musicIntent.also {intent ->
            intent.removeExtra("pathOfSong")
            intent.putExtra("pathOfSong", pathOfSong)
            activity!!.intent.removeExtra("pathOfSong")
            intent.putExtra("pathOfSong", pathOfSong)
            activity!!.bindService(musicIntent, connection, Context.BIND_AUTO_CREATE)
        }






//late in the button click










        getLabels()


        binding.playBtn.setBackgroundResource(R.drawable.stop)
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
                        currentPosition= progress
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

//        uiScope.launch(Dispatchers.IO ){
//            //asyncOperation
//            while(mService.getSongCurrPos()< totalTime) {
//
//                var elapsedtime = createTimeLabel(mService.getSongCurrPos())
//                //withContext(Dispatchers.Main) {
//                binding.elapsedTimeLabel.text = elapsedtime
//
//                //}
//            }

        mBound.observe(viewLifecycleOwner, Observer<Boolean>{ bound ->
            //Toast.makeText(this.context, "observe bound =" + bound, Toast.LENGTH_SHORT).show()
            if(bound==true){
                if(mService.getPath().equals(pathOfSong)){
                    currentPosition= mService.getSongCurrPos()
                }else{
                    mService.seekTo(0)
                    binding.elapsedTimeLabel.text = createTimeLabel(0)
                }
            }

        })

        // Thread
        Thread(Runnable {

            while (currentPosition <= totalTime) {
                try {

                    if(!paused){
                        var msg = Message()
                        this.currentPosition+=1000
                        msg.what = this.currentPosition
                        handler.sendMessage(msg)
                    }

                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Log.d("x", " caught interruptedException in thread runnable")
                }
            }
        }).start()






        return binding.root
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            //Log.d("x", "handler message received : " + msg.what)
            var currentPosition = msg.what

            // Update positionBar

            binding.positionBar.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            binding.elapsedTimeLabel.text = elapsedTime

//            var remainingTime = createTimeLabel(totalTime - currentPosition)
//            remainingTimeLabel.text = "-$remainingTime"
        }
    }



    override fun onStop() {
        Log.d("x", "fragment stopped")

        super.onStop()


    }


    override fun onDestroy() {
        Log.d("x", "fragment destroyed")
        fragmentJob.cancel()
        super.onDestroy()
        if(mBound.value!!)
            activity!!.unbindService(connection)
        mBound.value = false
        //mService.unbindService(connection)


        activity!!.supportFragmentManager.beginTransaction().remove(this@PlayingFragment).commit()
    }

    fun togglePlayButton(){
        if(mService.isPlaying()){
            binding.playBtn.setBackgroundResource(R.drawable.play)
            paused=true
            mService.startStopMedia()
        }else{
            binding.playBtn.setBackgroundResource(R.drawable.stop)
            paused=false
            mService.startStopMedia()
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
        var text = "$artist | $album"
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






}
