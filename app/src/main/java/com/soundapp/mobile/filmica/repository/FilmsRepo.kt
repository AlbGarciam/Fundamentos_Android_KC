package com.soundapp.mobile.filmica.repository

import android.content.Context
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.soundapp.mobile.filmica.BuildConfig
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes
import com.soundapp.mobile.filmica.repository.domain.AppDatabase
import com.soundapp.mobile.filmica.repository.domain.Film
import com.soundapp.mobile.filmica.repository.domain.NetworkUtilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Let's create a singleton on the app
object FilmsRepo {
    private val films : MutableList<Film> = mutableListOf()
    // Can be also declared as val films = mutableListOf<Film>()
    /*
        get() {// In order to avoid recursive calls, we must use field instead of films
            if (field.isEmpty())
                field.addAll(dummyFilms())
            return field
        }
    */

    @Volatile // La instancia de la memoria principal se actualiza con la BBDD
    private var database: AppDatabase? = null

    private fun getDbInstance(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    BuildConfig.MOVIE_DB_NAME).build()
        }
        return database as AppDatabase
    }

    fun findFilmBy(id: String): Film? {
        return films.find { it.id == id }
    }

    fun saveFilm(context: Context, film:Film, callback: (Film) -> Unit) {
        // Execute this process always on a different thread. Use CoRoutines!!!!
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().insertFilm(film)
            }
            async.await()
            // It won't continue the function until async finishes but it doesn't block the main thread
            callback.invoke(film)
        }
    }

    fun getStoredFilms(context: Context, callback: (List<Film>) -> Unit) {
        // Execute this process always on a different thread. Use CoRoutines!!!!
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().getFilms()
            }
            val films = async.await()
            // It won't continue the function until async finishes but it doesn't block the main thread
            callback.invoke(films)
        }
    }

    fun removeFilm(context: Context, film:Film, callback: () -> Unit) {
        // Execute this process always on a different thread. Use CoRoutines!!!!
        GlobalScope.launch(Dispatchers.Main) {
            val async = async(Dispatchers.IO) {
                val db = getDbInstance(context)
                db.filmDao().deleteFilm(film)
            }
            async.await()
            // It won't continue the function until async finishes but it doesn't block the main thread
            callback.invoke()
        }
    }

    fun discoverFilms(context: Context, onResponse: (List<Film>) -> Unit, onError: (Error) -> Unit ) {
        val url = ApiRoutes.discoverMoviesURL()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            var films = Film.parseFilms( response.getJSONArray("results") )
            FilmsRepo.films.clear()
            FilmsRepo.films.addAll(films)
            onResponse.invoke(films.toList())
        }, { error ->
            error.printStackTrace()
            onError.invoke(Error())
        })
        /*** WORKAROUND FOR ANDROID KITKAT AND OLDER ***/
        /***
         * https://stackoverflow.com/questions/42999914/com-android-volley-noconnectionerror-javax-net-ssl-sslexception-connection-clo
         ***/
        NetworkUtilities.updateAndroidSecurityProvider(context)
        /*** END - WORKAROUND FOR ANDROID KITKAT AND OLDER ***/
        Volley.newRequestQueue(context).add(request)
    }


    private fun dummyFilms() : MutableList<Film> {
        return (0..10).map { i ->  Film(id = "$i", title = "film $i", overview = "Overview $i") }.toMutableList()
    }
}