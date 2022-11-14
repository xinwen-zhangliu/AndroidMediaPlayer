package com.proyecto2_reproductor_de_musica.services

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.activities.MainActivity
import com.proyecto2_reproductor_de_musica.data.Constants.CHANNEL_ID
import com.proyecto2_reproductor_de_musica.data.Constants.MUSIC_NOTIFICATION_ID
import java.io.File


class PlayingService : Service() {

    private lateinit var musicPlayer : MediaPlayer
    private var path : String= ""

    //--------------------------------->
    //SONG METADATA
    var songTitle : String = ""
    var artist : String = ""
    var album : String = ""
    var image : Bitmap? = null
    //--------------------------------->

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
            showNotification(getSongViewComponent())
        }

        return binder

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
        showNotification(getSongViewComponent())
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
        try{
            musicPlayer.release()
        }catch (e:Exception){}
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(path.isNullOrEmpty()){

            showNotification(showEmptyNotification())
        }else{
            showNotification(getSongViewComponent())
        }
        return START_NOT_STICKY
    }

    override fun onCreate() {



        super.onCreate()
        createNotificationChannel()
    }

//    fun broadcast(myMessage:String){
//        val intent = Intent(MainActivity.RECEIVER_INTENT)
//        intent.putExtra(MainActivity.RECEIVER_MESSAGE, myMessage)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//    }


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

    private fun showEmptyNotification() : RemoteViews{
        val contentView = RemoteViews(packageName, R.layout.layout_notification_expanded)
        contentView.setTextViewText(R.id.title, "No song playing")
        contentView.setImageViewResource(R.id.largeIcon, R.drawable.image)
        return contentView
    }

    private fun getSongViewComponent() : RemoteViews{
        //Todo: implement functions for showing the notification
        updateMetadata()
        val contentView = RemoteViews(packageName, R.layout.layout_notification_expanded)
        contentView.setTextViewText(R.id.title, songTitle)
        contentView.setImageViewResource(R.id.largeIcon, R.drawable.image)
        if(this.image != null)
            contentView.setImageViewBitmap(R.id.largeIcon, image)
        contentView.setTextViewText(R.id.notificationSubtitle, "$artist | $album")
        return contentView

    }

    private fun showNotification(contentView : RemoteViews){
        val  notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent= PendingIntent.getActivity(
            this, 0, notificationIntent, 0
        )


        val notification = Notification
            .Builder(this, CHANNEL_ID)
            .setContentText("Music Player")
            .setSmallIcon(R.drawable.image)
            .setCustomContentView(contentView)
            .setCustomBigContentView(contentView)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        startForeground(MUSIC_NOTIFICATION_ID, notification)
    }

    private fun updateMetadata(){
        val mmr = MediaMetadataRetriever()
        try {
            var file = File(path)
            mmr.setDataSource(this,Uri.fromFile(file))
        }catch (e : IllegalArgumentException){
            Log.d("x", "couldt find song metadata playing service")
        }
        var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        var album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        if (title != null) {
            this.songTitle = title
        }else{
            this.songTitle = "unknown"
        }
        var text = "$artist | $album"
        this.artist = text
        var hasImage = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_COUNT)?.toInt()?:-1

        val artBytes = mmr.embeddedPicture

        if (artBytes != null) {
            val bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)

            image = bm
        }
//        if(hasImage==-1){
//            image.setImageResource(R.drawable.image)
//        }

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