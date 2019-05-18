package com.soundapp.mobile.filmica.screens.utils.paging

import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.repository.films.DataSourceRepository
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FilmsDataSource(private val repo: DataSourceRepository<Film>,
                      private val context: Context,
                      private val onError: (Error?) -> Unit): PageKeyedDataSource<Int, Film>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Film>) {
        val initialPage = 1

        val args = HashMap<String, String>()
        args[ApiRoutes.PAGE_PARAM] = initialPage.toString()
        // LoadInitialCallback must be synchronous
        runBlocking {
            val films: List<Film> = loadInitialItems(args)
            callback.onResult(films.toMutableList(), null, initialPage+1)
        }
    }

    private suspend fun loadInitialItems(args: HashMap<String, String>): List<Film> = suspendCoroutine { cont ->
        repo.get(params = args, context = context, callback = {
            cont.resume(it)
        }, error = {
            onError.invoke(it)
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        val page: Int = params.key
        val args = HashMap<String, String>()
        args[ApiRoutes.PAGE_PARAM] = page.toString()
        repo.get(params = args, context = context, callback = {
                    callback.onResult(it.toMutableList(), if (page == 1000) null else (page+1))
                }, error = {
                    onError.invoke(it)
                })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        val page: Int = params.key
        val args = HashMap<String, String>()
        args[ApiRoutes.PAGE_PARAM] = page.toString()
        repo.get(params = args,
                context = context,
                callback = {
                    callback.onResult(it.toMutableList(), if (page == 1) null else (page-1))
                },
                error = {
                    onError.invoke(it)
                })
    }

    private fun dummyFilms() : MutableList<Film> {
        return (0..10).map { i ->  Film(id = "$i", title = "film $i", overview = "Overview $i") }.toMutableList()
    }
}