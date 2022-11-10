package com.proyecto2_reproductor_de_musica.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import androidx.lifecycle.Observer
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.activities.MainActivity
import com.proyecto2_reproductor_de_musica.data.Constants.CHANNEL_ID
import com.proyecto2_reproductor_de_musica.data.Constants.MUSIC_NOTIFICATION_ID
import com.proyecto2_reproductor_de_musica.data.PlayingLiveData
import com.proyecto2_reproductor_de_musica.data.viewModels.PlayingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.File

class PlayingService : Service() {

    private lateinit var musicPlayer : MediaPlayer
    var liveData = PlayingLiveData()
    lateinit var playingViewModel : PlayingViewModel


    companion object{
        var musicPath : String = ""
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)


    override fun onDestroy() {
        super.onDestroy()
       // liveData.path.removeObserver(ober)
        serviceJob.cancel()
    }

    override fun onCreate() {
        super.onCreate()

        //initMusic()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()

        val observerStartStopMedia = Observer<String>{ data->
            musicPlayer.reset()
            initMusic()
        }

        val observerPlayPauseMedia = Observer<Boolean> {data ->
            startStopMedia()
        }

        liveData.path.observeForever(observerStartStopMedia)
        liveData.state.observeForever(observerPlayPauseMedia)


        return START_STICKY
    }


    private fun playPauseMedia(){

    }

    private fun startStopMedia(){
        if(musicPlayer.isPlaying){
            musicPlayer.stop()
        }else{
//            serviceScope.launch {
//                musicPlayer.start()
//                musicPlayer.currentPosition
//
//            }

            musicPlayer.start()
            Thread(Runnable {
                while (musicPlayer != null) {
                    try {
                        var msg = Message()
                        msg.what = musicPlayer.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                    }
                }
            }).start()

        }
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what
            liveData.setNewPosition(currentPosition)
        }
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

    private fun initMusic(){
        val song = File(liveData.path.value.toString())
        musicPlayer = MediaPlayer.create(this, Uri.fromFile(song))
        musicPlayer.isLooping = true
        musicPlayer.setVolume(0.5f, 0.5f)
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