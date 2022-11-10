package com.proyecto2_reproductor_de_musica

import android.app.Application
import android.content.Intent
import com.proyecto2_reproductor_de_musica.services.PlayingService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicPlayerApp : Application(){
    override fun onTerminate() {
        super.onTerminate()
        stopService(Intent(this, PlayingService::class.java))
    }
}
