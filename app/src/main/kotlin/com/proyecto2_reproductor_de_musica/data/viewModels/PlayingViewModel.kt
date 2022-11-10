package com.proyecto2_reproductor_de_musica.data.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayingViewModel(application: Application) : ViewModel(){

    private val  _position : MutableLiveData<Int> = MutableLiveData()

    val position : LiveData<Int>
        get( ) = _position


    private val _path : MutableLiveData<String> = MutableLiveData()

    val path : LiveData<String>
        get() = _path

    companion object{


    }

    suspend fun addToCounter(){
        viewModelScope.launch {
            delay(50)
            _position.postValue(1)
        }
    }

    fun updatePosition(newPosition : Int){
        _position.postValue(newPosition)
    }

    fun getPositionLiveData() : LiveData<Int>{
        return position
    }

}