package com.soundapp.mobile.filmica.repository.domain

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Film::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun filmDao(): FilmDao
}