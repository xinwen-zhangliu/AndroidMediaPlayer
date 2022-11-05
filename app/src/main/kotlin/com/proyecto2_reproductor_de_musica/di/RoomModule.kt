package com.proyecto2_reproductor_de_musica.di

import android.content.Context
import androidx.room.Room
import com.proyecto2_reproductor_de_musica.data.db.MediaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//Todo: ask about dependency injection,


@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    private const val MEDIA_DATABASE_NAME = "media_database"
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(context,MediaDatabase::class.java,
        MEDIA_DATABASE_NAME )



    @Singleton
    @Provides
    fun provideSongDao(db:MediaDatabase)= db.getSongDao()

}