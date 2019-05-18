package com.soundapp.mobile.filmica.screens.utils.paging

import android.content.Context
import androidx.paging.DataSource
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.repository.films.DataSourceRepository

class FilmsDataSourceFactory(val repository: DataSourceRepository<Film>, val context: Context): DataSource.Factory<Int, Film>() {
    private lateinit var filmsDataSource: FilmsDataSource

    override fun create(): DataSource<Int, Film> {
        filmsDataSource = FilmsDataSource(repository, context)
        return filmsDataSource
    }
}