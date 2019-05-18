package com.soundapp.mobile.filmica.repository

import android.content.Context
import androidx.room.Room
import com.soundapp.mobile.filmica.BuildConfig
import com.soundapp.mobile.filmica.repository.domain.film.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Let's create a singleton on the app
object FilmsRepo {
    private val films: MutableList<Film> = mutableListOf()
    private val trendingFilms: MutableList<Film> = mutableListOf()
    private val lastSearchedFilms: MutableList<Film> = mutableListOf()
    private val localFilms: MutableList<Film> = mutableListOf()

    @Volatile // La instancia de la memoria principal se actualiza con la BBDD
    private var database: AppDatabase? = null

    fun findFilmBy(id: String) = films.find { it.id == id } ?:
        trendingFilms.find { it.id == id } ?:
        lastSearchedFilms.find { it.id == id } ?:
        localFilms.find { it.id == id }

    /** Database layer **/

    private fun getDbInstance(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    BuildConfig.MOVIE_DB_NAME).build()
        }
        return database as AppDatabase
    }

    fun saveFilm(context: Context, film: Film, callback: (Film) -> Unit = {}) {
        // Execute this process always on a different thread. Use CoRoutines!!!!
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().insertFilm(film)
                getStoredFilms(context) // Update cached items
            }
            async.await()
            // It won't continue the function until async finishes but it doesn't block the main thread
            callback.invoke(film)
        }
    }

    fun getStoredFilms(context: Context, callback: (List<Film>) -> Unit = {}) {
        // Execute this process always on a different thread. Use CoRoutines!!!!
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilms()
            }
            val films = async.await()
            localFilms.clear()
            localFilms.addAll(films)
            // It won't continue the function until async finishes but it doesn't block the main thread
            callback.invoke(localFilms)
        }
    }

    fun removeFilm(context: Context, film: Film, callback: () -> Unit = {}) {
        // Execute this process always on a different thread. Use CoRoutines!!!!
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().deleteFilm(film)
                getStoredFilms(context) // Update cached items
            }
            async.await()
            // It won't continue the function until async finishes but it doesn't block the main thread
            callback.invoke()
        }
    }
}