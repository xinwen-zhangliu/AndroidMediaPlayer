package com.proyecto2_reproductor_de_musica.data.useCases

import com.proyecto2_reproductor_de_musica.data.repositories.MediaRepository
import javax.inject.Inject

class GetMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
//    suspend operator fun invoke():List<MediaItemData>{
//        val media = repository.getAllMediaFromDatabase()
//        return if(media.isNotEmpty()){
//            repository.insertMedia(media.map { it.toDatabase() })
//            media
//        }else{
//            media
//        }
//
//    }
}