package com.proyecto2_reproductor_de_musica.data.models

import com.google.gson.annotations.SerializedName

data class MediaItemModel(
  @SerializedName("title") val title: String,
  @SerializedName("performer") val performer: String,
)