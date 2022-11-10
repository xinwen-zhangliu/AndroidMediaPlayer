package com.proyecto2_reproductor_de_musica.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.properties.Delegates

class PlayingLiveData {
    private val  _position : MutableLiveData<Int> = MutableLiveData()

    val position : LiveData<Int>
        get( ) = _position


    private val _path : MutableLiveData<String> = MutableLiveData()

    val path : LiveData<String>
        get() = _path


    private val _state : MutableLiveData<Boolean> = MutableLiveData()
    //True = is playing
    //False = is paused

    val state : LiveData<Boolean>
        get() = _state

    var totalTime by Delegates.notNull<Int>()

    private val _userPosition : MutableLiveData<Int> = MutableLiveData()

    val userPosition : LiveData<Int>
        get() = _userPosition


//    private val _finished : MutableLiveData<Boolean> = MutableLiveData()
//
//    val finished : LiveData<Boolean>
//        get() = _finished

    val finished : MutableLiveData<Boolean> = MutableLiveData()



    fun setFinished(isfinished : Boolean){
        finished.value = isfinished
    }

    fun setNewUserPosition(newPosition : Int){
        _userPosition.value = newPosition
    }

    fun setNewPosition(newPosition : Int){
       _position.value = newPosition
    }

    fun setNewPath(newPath : String ){
        _path.value = newPath
    }



    fun setNewState(newState : Boolean){
        _state.value = newState
    }


    //  lateinit var navListFragmentInstance : PlayingFragment

}