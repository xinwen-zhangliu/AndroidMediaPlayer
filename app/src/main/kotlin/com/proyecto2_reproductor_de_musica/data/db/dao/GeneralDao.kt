package com.proyecto2_reproductor_de_musica.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.proyecto2_reproductor_de_musica.data.db.entities.AlbumsEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.PerformerEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.SongEntity
import com.proyecto2_reproductor_de_musica.data.db.entities.TypesEntity

@Dao
interface GeneralDao {
    //Song
    @Query("SELECT * FROM rolas_table ORDER BY id_rola DESC")
    fun getAllSongs(): LiveData<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAllSongs(songsToInsert:List<SongEntity> )


//    @Transaction
//    @Query("SELECT * FROM rolas_table WHERE id_album = :id ")
//    suspend fun getAlbumWithSongs(id : Int):List<AlbumWithSongs>


    //Types
    @Insert
    suspend fun insertTypes(types: List<TypesEntity>)

    @Query("SELECT * FROM types_table")
    fun getAllTypes(): List<TypesEntity>


    //Album
    @Query("SELECT * FROM albums_table ORDER BY id_album DESC")
    @JvmSuppressWildcards
    fun getAllAlbums(): LiveData<List<AlbumsEntity>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAllAlbums(albumsToInsert:List<AlbumsEntity> )


    //Performers
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAllPerformers(performersToInsert:List<PerformerEntity> )


}