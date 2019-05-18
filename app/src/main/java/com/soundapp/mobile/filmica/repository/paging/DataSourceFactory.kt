package com.soundapp.mobile.filmica.repository.paging

import android.content.Context
import androidx.paging.DataSource
import com.soundapp.mobile.filmica.repository.domain.film.Film

class DataSourceFactory : DataSource.Factory<Int, Film>() {

    private var listener: FilmsDataSource.FilmsDataSourceListener? = null
    private lateinit var filmsDataSource: FilmsDataSource

    fun setContext(context: Context) {
        if (context is FilmsDataSource.FilmsDataSourceListener) {
            listener = context
        } else {
            throw IllegalArgumentException("The attached context does not implement ${FilmsDataSource.FilmsDataSourceListener::class.java.canonicalName}")
        }
    }

    override fun create(): DataSource<Int, Film> {
        filmsDataSource = FilmsDataSource(listener)
        return filmsDataSource
    }

}