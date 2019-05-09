package com.soundapp.mobile.filmica.repository

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.soundapp.mobile.filmica.repository.domain.Film

// Let's create a singleton on the app
object FilmsRepo {
    val films : MutableList<Film> = mutableListOf() // Can be also declared as val films = mutableListOf<Film>()
        get() {// In order to avoid recursive calls, we must use field instead of films
            if (field.isEmpty())
                field.addAll(dummyFilms())
            return field
        }

    fun findFilmBy(id: String): Film? {
        return films.find { it.id == id }
    }

    fun discoverFilms(context: Context, onResponse: (List<Film>) -> Unit, onError: (Error) -> Unit ) {
        val url = Uri.Builder()
                .scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", "bfcc8dbc9a5d11fb48ca612ce987bb47")
                .appendPath("discover")
                .appendPath("movie")
                .build()
                .toString()

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            var films = mutableListOf<Film>()
            val filmsArray = response.getJSONArray("results")
            for (i in 0..(filmsArray.length() - 1)) {
                films.add(Film.parseFilm(filmsArray.getJSONObject(i)))
            }
            onResponse.invoke(films.toList())
        }, { error ->
            error.printStackTrace()
            onError.invoke(error as Error)
        })
        Volley.newRequestQueue(context).add(request)
    }


    private fun dummyFilms() : MutableList<Film> {
        return (0..10).map { i ->  Film(id = "$i", title = "film $i", overview = "Overview $i") }.toMutableList()
    }
}