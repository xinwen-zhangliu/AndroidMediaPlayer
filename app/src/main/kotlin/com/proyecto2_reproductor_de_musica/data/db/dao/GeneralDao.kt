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


//    @Transaction
//    @Query("SELECT * FROM rolas_table WHERE id_album = :id ")
//    suspend fun getAlbumWithSongs(id : Int):List<AlbumWithSongs>


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

    @Query("SELECT * FROM albums_table WHERE name =:nameSearch LIMIT 1")
    suspend fun searchAlbumByName(nameSearch : String) : AlbumsEntity

    @Query("SELECT * FROM albums_table WHERE id_album =:idToSearch LIMIT 1")
    suspend fun getAlbumById(idToSearch:Int): AlbumsEntity

    @Query("SELECT * FROM albums_table WHERE rowid =:rowId  LIMIT 1")
    suspend fun albumFromRow(rowId: Int) : AlbumsEntity


    @Query("SELECT id_album FROM albums_table ORDER BY id_album DESC LIMIT 1")
    fun getLastAlbumId(): List<Long>

    @Query("SELECT * FROM albums_table WHERE name =:name LIMIT 1")
    suspend fun getAlbumFromName(name : String) : List<AlbumsEntity>


    //Performers
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertAllPerformers(performersToInsert:List<PerformerEntity> )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOnePerformer(performer : PerformerEntity) : Long

    @Query("SELECT MAX(id_performer) FROM performer_table")
    suspend fun getLastIdPerformer():Int

    @Query("SELECT * FROM performer_table WHERE id_performer =:idToSearch LIMIT 1")
    suspend fun getPerformerById(idToSearch:Int): PerformerEntity

    @Query("SELECT * FROM performer_table WHERE rowid =:rowId LIMIT 1")
    suspend fun performerFromRow(rowId: Int) : PerformerEntity

    @Query("SELECT id_performer FROM performer_table ORDER BY id_performer DESC LIMIT 1")
    fun getLastPerformerId(): List<Long>

    @Query("SELECT * FROM performer_table WHERE name =:name LIMIT 1")
    suspend fun getPerformerFromName(name : String) : List<PerformerEntity>



}