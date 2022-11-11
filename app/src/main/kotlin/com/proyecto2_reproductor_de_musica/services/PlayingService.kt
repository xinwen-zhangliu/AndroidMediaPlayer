package com.proyecto2_reproductor_de_musica.services

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.activities.MainActivity
import com.proyecto2_reproductor_de_musica.data.Constants.CHANNEL_ID
import com.proyecto2_reproductor_de_musica.data.Constants.MUSIC_NOTIFICATION_ID
import com.proyecto2_reproductor_de_musica.data.repositories.LiveDataRepository
import com.proyecto2_reproductor_de_musica.data.viewModels.PlayingViewModel
import java.io.File

class PlayingService : Service() {

    private lateinit var musicPlayer : MediaPlayer

    lateinit var playingViewModel : PlayingViewModel
    private val liveDataRepo = LiveDataRepository()
    //lateinit var viewModel: PlayingViewModel by viewModels()

    private var path : String= ""
    //--------------------------------//
    private val binder = LocalBinder()

    /**
     * Creating necessary stuff to bind activities to the service and control childfragments
     */
    inner class LocalBinder : Binder(){
        /**
         * returns instance of PlayingService
         */
        fun getService():PlayingService = this@PlayingService
    }

    override fun onBind(intent: Intent): IBinder {
        path = intent.getStringExtra("pathOfSong").toString()
        initMusic()
        return binder
    }
    //---------------------------------//



//    private val serviceJob = Job()
//    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)


    override fun onDestroy() {
        super.onDestroy()
        //serviceJob.cancel()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }


    private fun initMusic(){
        val song = File(path)
        musicPlayer = MediaPlayer.create(this, Uri.fromFile(song))
        musicPlayer.isLooping = true
        musicPlayer.setVolume(0.5f, 0.5f)
        musicPlayer.start()
    }

    fun getSongCurrPos() : Int{
        return musicPlayer.currentPosition
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()

        return START_STICKY
    }




    fun startStopMedia(){
        if(musicPlayer.isPlaying){
            musicPlayer.stop()
        }else{

            try{
                musicPlayer.start()
            }catch (e : java.lang.IllegalStateException){
                Log.d("x", "Playing Service, MediaPlayer is om invalid state")
            }

        }
    }

    fun seekTo(place : Int){
        musicPlayer.seekTo(place)
    }


    fun isPlaying() : Boolean{
        return musicPlayer.isPlaying
    }


    private fun showNotification(){
        val  notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent= PendingIntent.getActivity(
            this, 0, notificationIntent, 0
        )

        val notification = Notification
            .Builder(this, CHANNEL_ID)
            .setContentText("Music Player")
            .setSmallIcon(R.drawable.image)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(MUSIC_NOTIFICATION_ID, notification)
    }



    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "PlayingService",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(serviceChannel)
        }
    }


}