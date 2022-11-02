package com.proyecto2_reproductor_de_musica.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        private  const val  DATABASE_NAME = "songsDatabse"
        private  const val  TABLE_TYPES = "tableTypes"
        private const val TABLE_PERFORMERS = "tablePerformers"
        private const val TABLE_PERSONS =  "tablePersons"
        private const val TABLE_GROUPS= "tableGroups"
        private const val  TABLE_ALBUMS = "tableAlbums"
        private const val  TABLE_ROLAS = "tableRolas"
        private const val  TABLE_IN_GROUPS = "tableInGroups"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}