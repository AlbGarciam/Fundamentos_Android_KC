package com.soundapp.mobile.filmica.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.repository.domain.film.FilmDao

@Database(entities = [Film::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun filmDao(): FilmDao
}