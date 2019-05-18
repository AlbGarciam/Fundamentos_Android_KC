package com.soundapp.mobile.filmica.repository.films

import android.content.Context
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes.PAGE_PARAM
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes.QUERY_PARAM
import com.soundapp.mobile.filmica.repository.NetworkUtilities
import com.soundapp.mobile.filmica.repository.domain.film.Film

object SearchRepository: DataSourceRepository<Film> {
    var searchedText: String = ""

    override fun get(params: HashMap<String, String>, context: Context, callback: ((List<Film>) -> Unit)?, error: ((Error?) -> Unit)?) {
        params[QUERY_PARAM] = searchedText
        if (searchedText.count() < 3 || (params[PAGE_PARAM]?.toInt() ?: 0) > 1) {
            callback?.invoke(listOf())
            return
        }
        val url = ApiRoutes.searchMovies(params)
        NetworkUtilities.GET_LIST(context, url,{
            var films = Film.parseFilms(it)
            if (films.count() > 10) {
                films = films.slice(0 until 10)
            }
            // TheMovieDB does not provide a way to limit the number of results.
            // It only returns the data on pages of 20 elements, so in order to fit with the requirements,
            // we are going to add the filter of 10 items on the front
            callback?.invoke(films)
        }, {
            error?.invoke(it)
        })
    }
}