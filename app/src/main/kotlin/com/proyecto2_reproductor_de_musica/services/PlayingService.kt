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
import java.io.File

class PlayingService : Service() {

    private lateinit var musicPlayer : MediaPlayer



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
        if(path.equals("")){
            path = intent.getStringExtra("pathOfSong").toString()
            initMusic()
        }

        return binder

//        path = intent.getStringExtra("pathOfSong").toString()
//        initMusic()
//        return binder
    }
    //---------------------------------//


    override fun onUnbind(intent: Intent?): Boolean {


        return true

    }
    fun setPath(newPath : String){
        if(path.equals(newPath))
            return
        path = newPath
        musicPlayer.release()
        initMusic()
    }
    fun getPath():String{
        return this.path
    }

    override fun onRebind(intent: Intent?) {
        Log.d("x", "on rebind playing Service")
        //super.onRebind(intent)

        onBind(intent!!)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
//        val notification = Notification()
//        notification.tickerText = "Music Player ticker text"
//        notification.icon = R.drawable.image
//        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
////        notification.setLatestEventInfo(
////            applicationContext, "MusicPlayerSample",
////            "Playing: $songName", pi
////        )
////
////        notification.
//        startForeground(MUSIC_NOTIFICATION_ID, notification)

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
    fun getMediaPlayer() : MediaPlayer{
        Log.d("x", " getting media player")
        return musicPlayer
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()

        return START_STICKY
    }




    fun startStopMedia(){
        if(musicPlayer.isPlaying){
            musicPlayer.pause()
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