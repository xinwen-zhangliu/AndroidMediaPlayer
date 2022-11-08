package com.proyecto2_reproductor_de_musica.fragments.playing

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.proyecto2_reproductor_de_musica.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "title"
private const val ARG_PARAM2 = "subtitle"
private const val ARG_PARAM3 = "time"
private const val ARG_PARAM4 = "path"

/**
 * A simple [Fragment] subclass.
 * Use the [PlayingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = null
    private var subtitle: String? = null
    private var time : Int= 0
    private var path: String? = null


    val volumeBar = view?.findViewById<SeekBar>(R.id.volumeBar)
    val positionBar = view?.findViewById<SeekBar>(R.id.positionBar)
    val playBtn = view?.findViewById<Button>(R.id.playBtn)
    val elapsedTimeLabel = view?.findViewById<TextView>(R.id.elapsedTimeLabel)
    val remainingTimeLabel = view?.findViewById<TextView>(R.id.remainingTimeLabel)


    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

        @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            positionBar!!.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel!!.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel!!.text = "-$remainingTime"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            title = it.getString(ARG_PARAM1)
            subtitle    = it.getString(ARG_PARAM2)
            time    = it.getInt(ARG_PARAM3)
            path = it.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_playing, container, false)
        //Todo: check why this was throwing error
        view.findViewById<TextView>(R.id.songTitle).text = title
        view.findViewById<TextView>(R.id.author).text = subtitle
        //Todo: fix the media player
        //mp = MediaPlayer.create(this, R.raw.cage_the_elephant_trouble)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration

        playBtn!!.setOnClickListener(
            View.OnClickListener {
                @Override
                fun OnClick(view: View?){
                    if (mp.isPlaying) {
                        // Stop
                        mp.pause()
                        view!!.findViewById<Button>(R.id.playBtn).setBackgroundResource(R.drawable.play)

                    } else {
                        // Start
                        mp.start()
                        view!!.findViewById<Button>(R.id.playBtn).setBackgroundResource(R.drawable.stop)
                    }
                }
            }
        )






         //Volume Bar
        volumeBar!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    //audioManager= getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    if (fromUser) {
                        var volumeNum :Float = progress / 100.0f
                        mp.setVolume(volumeNum, volumeNum)
                        //var newVolume : Int = volumeNum.toInt()

                        if (progress<0){

                            //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,newVolume,AudioManager.FLAG_PLAY_SOUND )
                        }else{

                        }
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // Position Bar
        positionBar!!.max = totalTime
        positionBar!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )



        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()

        return view
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlayingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String, subtitle: String) =
            PlayingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, subtitle)
                    putInt(ARG_PARAM3, time)
                    putString(ARG_PARAM4, path)

                }
            }
    }





}

private fun Button.setOnClickListener(view: View?) {
    var playBtn = view!!.findViewById<Button>(R.id.playBtn)

}
