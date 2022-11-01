package com.proyecto2_reproductor_de_musica

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import res.layout.*


class MainActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0
    lateinit var audioManager: AudioManager
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    
        mp = MediaPlayer.create(this, R.raw.cage_the_elephant_trouble)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration


        isPermissionGranted()
        getLabels()

        // Volume Bar
        volumeBar.setOnSeekBarChangeListener(
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
        positionBar.max = totalTime
        positionBar.setOnSeekBarChangeListener(
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
    }



    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            positionBar.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"
        }
    }

    fun isPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ),
                5
            )
            false
        } else {
            true
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

    @RequiresApi(Build.VERSION_CODES.P)
    fun getLabels(){
        val mmr = MediaMetadataRetriever()



        try {

            mmr.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/raw/cage_the_elephant_trouble"))

            //mmr.setDataSource(this, uri)
        }catch (e : IllegalArgumentException){
            //title = "error"
        }
        var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        songTitle.text = title
        author.text = artist
        var hasImage = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_COUNT)?.toInt()?:-1
        if(hasImage>0  ){
            var imageAtIndex:Int =
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_PRIMARY)?.toInt() ?:-1
            //if(imageAtIndex!=-1)
                songImage.setImageBitmap(mmr.getImageAtIndex(imageAtIndex) )

        }

        val artBytes = mmr.embeddedPicture
        if (artBytes != null) {
            val bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
            songImage.setImageBitmap(bm)
        }

    }

    fun playBtnClick(v: View) {

        if (mp.isPlaying) {
            // Stop
            mp.pause()
            playBtn.setBackgroundResource(R.drawable.play)

        } else {
            // Start
            mp.start()
            playBtn.setBackgroundResource(R.drawable.stop)
        }
    }

}