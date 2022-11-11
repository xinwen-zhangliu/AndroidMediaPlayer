package com.proyecto2_reproductor_de_musica.data.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyecto2_reproductor_de_musica.data.repositories.LiveDataRepository

class PlayingViewModel() : ViewModel(){

    private val liveDataRepo = LiveDataRepository()

    private var _position : MutableLiveData<Int> = liveDataRepo.position
    val  position : MutableLiveData<Int> = _position

//    private var _path : MutableLiveData<String> = liveDataRepo.path
//    val path : MutableLiveData<String> = _path

    private var _state :MutableLiveData<Boolean> = liveDataRepo.state
    val state : MutableLiveData<Boolean> = _state

    private var _userPosition : MutableLiveData<Int> = liveDataRepo.userPosition
    val userPosition : MutableLiveData<Int> = _userPosition

    private var _finished : MutableLiveData<Boolean> = liveDataRepo.finished
    val finished : MutableLiveData<Boolean> = _finished


    var path : MutableLiveData<String> = MutableLiveData()

    //FUNCTIONS TO SET VALUES
    fun setFinished(isfinished : Boolean){
        finished.value = isfinished
    }

    fun setNewUserPosition(newPosition : Int){
        userPosition.value = newPosition
    }

    fun setNewPosition(newPosition : Int){
        position.value = newPosition
    }

    fun setNewPath(newPath : String ){
       // liveDataRepo.path.value = newPath
        path.value = newPath
        Log.d("d", "PlayingLiveData, you set a nuew path: " + newPath)
    }

    fun setNewState(newState : Boolean){
        state.value = newState
    }

//    init {
//        liveDataRepo.path.observeForever { path ->
//            this._path.value  = path
//        }
//    }
}