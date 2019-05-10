package com.soundapp.mobile.filmica.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes
import com.soundapp.mobile.filmica.repository.domain.Film
import com.soundapp.mobile.filmica.repository.domain.NetworkUtilities

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

    fun findFilmBy(id: String): Film? {
        return films.find { it.id == id }
    }

    fun discoverFilms(context: Context, onResponse: (List<Film>) -> Unit, onError: (Error) -> Unit ) {
        val url = ApiRoutes.discoverMoviesURL()
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            var films = Film.parseFilms( response.getJSONArray("results") )
            this.films.addAll(films)
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
        Volley.newRequestQueue(context).add(request)
    }


    private fun dummyFilms() : MutableList<Film> {
        return (0..10).map { i ->  Film(id = "$i", title = "film $i", overview = "Overview $i") }.toMutableList()
    }
}