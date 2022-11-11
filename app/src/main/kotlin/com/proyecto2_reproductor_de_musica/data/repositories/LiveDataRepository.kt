package com.proyecto2_reproductor_de_musica.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LiveDataRepository {
    val  position : MutableLiveData<Int> = MutableLiveData()
    val path : MutableLiveData<String> = MutableLiveData()
    val state : MutableLiveData<Boolean> = MutableLiveData()
    val userPosition : MutableLiveData<Int> = MutableLiveData()
    val finished : MutableLiveData<Boolean> = MutableLiveData()


    private val _countryLiveData : MutableLiveData<String> = MutableLiveData()

    // obj2
    val countryLiveData : LiveData<String>
        get() = _countryLiveData




}