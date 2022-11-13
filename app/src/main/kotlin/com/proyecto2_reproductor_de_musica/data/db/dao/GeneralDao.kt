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
    @Query("SELECT * FROM rolas_table")
    fun getAllSongs(): LiveData<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAllSongs(songsToInsert:List<SongEntity> )

    @Query("SELECT * FROM rolas_table WHERE id_rola =:idToSearch LIMIT 1")
    suspend fun getSongById(idToSearch:Int): List<SongEntity>

    @Update
    suspend fun updateSong(song : SongEntity)

    @Delete
    suspend fun deleteSong(song : SongEntity)





    //Types
    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOneAlbum(album : AlbumsEntity) : Long

    @Query("SELECT MAX(id_album) FROM albums_table")
    suspend fun getLastIdAlbum():Int

    @Query("SELECT * FROM albums_table WHERE id_album =:idToSearch LIMIT 1")
    suspend fun getAlbumById(idToSearch:Int): List<AlbumsEntity>


    @Query("SELECT * FROM albums_table WHERE name =:name LIMIT 1")
    suspend fun getAlbumFromName(name : String) : List<AlbumsEntity>

    @Query("SELECT * FROM albums_table WHERE LOWER(name) =:name LIMIT 1")
    suspend fun getAlbumFromNameLowercase(name : String) : List<AlbumsEntity>

    @Update
    suspend fun updateAlbum(album: AlbumsEntity )

    @Delete
    suspend fun deleteAlbum(album: AlbumsEntity)


    //Performers
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAllPerformers(performersToInsert:List<PerformerEntity> )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOnePerformer(performer : PerformerEntity) : Long

    @Query("SELECT MAX(id_performer) FROM performer_table")
    suspend fun getLastIdPerformer():Int

    @Query("SELECT * FROM performer_table WHERE id_performer =:idToSearch LIMIT 1")
    suspend fun getPerformerById(idToSearch:Int): List<PerformerEntity>


    @Query("SELECT id_performer FROM performer_table ORDER BY id_performer DESC LIMIT 1")
    fun getLastPerformerId(): List<Long>

    @Query("SELECT * FROM performer_table WHERE name =:name  LIMIT 1")
    suspend fun getPerformerFromName(name : String) : List<PerformerEntity>

    @Query("SELECT * FROM performer_table WHERE LOWER(name) =:name  LIMIT 1")
    suspend fun getPerformerFromNameLowercase(name : String) : List<PerformerEntity>

    @Update
    suspend fun updatePerformer(performer: PerformerEntity)

    @Delete
    suspend fun deletePerformer(performer: PerformerEntity)


}