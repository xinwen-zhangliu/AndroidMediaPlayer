package com.proyecto2_reproductor_de_musica.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proyecto2_reproductor_de_musica.data.db.dao.GeneralDao
import com.proyecto2_reproductor_de_musica.data.db.dao.RawDao
import com.proyecto2_reproductor_de_musica.data.db.dao.RelationsDao
import com.proyecto2_reproductor_de_musica.data.db.entities.*

/**
 * Contains the database holder and serves as the main access point for the underlying connection
 * to the app's persisted, relational data.
 */
@Database(entities =
    [SongEntity::class,
    TypesEntity::class,
    AlbumsEntity::class,
    InGroupEntity::class,
    PerformerEntity::class,
    PersonsEntity::class,
    GroupsEntity::class],
    version=1,
    exportSchema = false)
abstract class MediaDatabase:RoomDatabase() {
    abstract fun getGeneralDao():GeneralDao
    abstract fun getRawDao():RawDao
    abstract fun getRelationsDao():RelationsDao

    companion object{
        private const val MEDIA_DATABASE_NAME = "media_database"
        @Volatile
        private var INSTANCE : MediaDatabase? = null

        fun  getDatabase(context : Context):MediaDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    MediaDatabase::class.java,
                    MEDIA_DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}