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
    private val films: MutableList<Film> = mutableListOf()
    private val trendingFilms: MutableList<Film> = mutableListOf()
    private val lastSearchedFilms: MutableList<Film> = mutableListOf()

    @Volatile // La instancia de la memoria principal se actualiza con la BBDD
    private var database: AppDatabase? = null

    fun findFilmBy(id: String) = films.find { it.id == id } ?: trendingFilms.find { it.id == id } ?: lastSearchedFilms.find { it.id == id }

    /** Network layer **/
    fun discoverFilms(context: Context, onResponse: (List<Film>) -> Unit, onError: (Error) -> Unit) {
        val url = ApiRoutes.discoverMoviesURL()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val films = Film.parseFilms(response.getJSONArray("results"))
            FilmsRepo.films.clear()
            FilmsRepo.films.addAll(films)
            onResponse.invoke(films.toList())
        }, { error ->
            error.printStackTrace()
            onError.invoke(Error())
        })
        makeRequest(context, request)
    }

    fun searchFilms(context: Context, title: String, onResponse: (List<Film>) -> Unit, onError: (Error) -> Unit) {
        val url = ApiRoutes.searchMoviesURL(title)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val films = Film.parseFilms(response.getJSONArray("results"))
            FilmsRepo.lastSearchedFilms.clear()
            FilmsRepo.lastSearchedFilms.addAll(films)
            onResponse.invoke(films.toList())
        }, { error ->
            error.printStackTrace()
            onError.invoke(Error())
        })
        makeRequest(context, request)
    }

    fun getTrendingFilms(context: Context, onResponse: (List<Film>) -> Unit, onError: (Error) -> Unit) {
        val url = ApiRoutes.trendingMoviesURL()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val trendingFilms = Film.parseFilms(response.getJSONArray("results"))
            FilmsRepo.trendingFilms.clear()
            FilmsRepo.trendingFilms.addAll(trendingFilms)
            onResponse.invoke(trendingFilms.toList())
        }, { error ->
            error.printStackTrace()
            onError.invoke(Error())
        })
        makeRequest(context, request)
    }

    private fun makeRequest(context: Context, request: JsonObjectRequest) {
        /*** WORKAROUND FOR ANDROID KITKAT AND OLDER
         * https://stackoverflow.com/questions/42999914/com-android-volley-noconnectionerror-javax-net-ssl-sslexception-connection-clo
         ***/
        NetworkUtilities.updateAndroidSecurityProvider(context)
        /*** END - WORKAROUND FOR ANDROID KITKAT AND OLDER ***/
        Volley.newRequestQueue(context).add(request)
    }
    /** End of Network layer **/
    /** Database layer **/

    private fun getDbInstance(context: Context): AppDatabase {
        if (database == null) {
            database = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    BuildConfig.MOVIE_DB_NAME).build()
        }
        return database as AppDatabase
    }

    fun saveFilm(context: Context, film: Film, callback: (Film) -> Unit) {
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

    fun removeFilm(context: Context, film: Film, callback: () -> Unit) {
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
}