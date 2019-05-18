package com.soundapp.mobile.filmica.repository.films

import android.content.Context
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes
import com.soundapp.mobile.filmica.repository.domain.NetworkUtilities
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.repository.paging.datasourcerepositories.DataSourceRepository

object SearchRepository: DataSourceRepository<Film> {
    override fun get(params: HashMap<String, String>, context: Context, callback: ((List<Film>) -> Unit)?, error: ((Error?) -> Unit)?) {
        val url = ApiRoutes.searchMovies(params)
        NetworkUtilities.GET_LIST(context, url,{
            val films = Film.parseFilms(it)
            // TheMovieDB does not provide a way to limit the number of results.
            // It only returns the data on pages of 20 elements, so in order to fit with the requirements,
            // we are going to add the filter of 10 items on the front
            callback?.invoke(films.slice(0 until 10))
        }, {
            error?.invoke(it)
        })
    }
}