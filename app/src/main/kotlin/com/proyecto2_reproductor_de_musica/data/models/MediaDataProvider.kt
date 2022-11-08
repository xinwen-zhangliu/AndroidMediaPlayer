
package com.proyecto2_reproductor_de_musica.data.models

import android.content.Context
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity


class MediaDataProvider (context: Context){


    companion object{
        var songsList = mutableListOf<SongEntity>()
        var albumList = mutableListOf<AlbumsEntity>()
        var performerList = mutableListOf<PerformerEntity>()
    }


}