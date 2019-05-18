package com.soundapp.mobile.filmica.repository.paging

import androidx.paging.PageKeyedDataSource
import com.soundapp.mobile.filmica.repository.domain.film.Film

class FilmsDataSource(val listener: FilmsDataSourceListener?): PageKeyedDataSource<Int, Film>() {
    interface FilmsDataSourceListener {
        fun loadPage(page: Int, callback: (List<Film>) -> Unit)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Film>) {
        listener?.loadPage(0) {films ->
            callback.onResult(films.toMutableList(), null, 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        val page: Int = params.key.toInt()
        listener?.loadPage(page) { films ->
            val next = page + 1
            callback.onResult(films.toMutableList(), next)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        val page: Int = params.key.toInt()
        listener?.loadPage(page) { films ->
            val previous = if (page == 0) null else (page - 1)
            callback.onResult(films.toMutableList(), previous)
        }
    }
}