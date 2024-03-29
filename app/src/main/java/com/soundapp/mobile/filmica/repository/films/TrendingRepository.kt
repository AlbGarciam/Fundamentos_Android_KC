package com.soundapp.mobile.filmica.repository.films

import android.content.Context
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes
import com.soundapp.mobile.filmica.repository.NetworkUtilities
import com.soundapp.mobile.filmica.repository.domain.film.Film

object TrendingRepository: DataSourceRepository<Film> {
    override fun get(params: HashMap<String, String>, context: Context, callback: ((List<Film>) -> Unit)?, error: ((Error?) -> Unit)?) {
        val url = ApiRoutes.trendingMovies(params)
        NetworkUtilities.GET_LIST(context, url,{
            val films = Film.parseFilms(it)
            callback?.invoke(films)
        }, {
            error?.invoke(it)
        })
    }
}