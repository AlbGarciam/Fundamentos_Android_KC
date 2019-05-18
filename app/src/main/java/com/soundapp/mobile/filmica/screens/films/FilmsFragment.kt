package com.soundapp.mobile.filmica.screens.films

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes.QUERY_PARAM
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.repository.films.SearchRepository
import com.soundapp.mobile.filmica.screens.utils.FilmicaTextWatcher
import com.soundapp.mobile.filmica.screens.utils.FilmsLiveDataObserver
import com.soundapp.mobile.filmica.screens.utils.FilmsOffsetDecorator
import com.soundapp.mobile.filmica.screens.utils.paging.FilmsDataSourceFactory
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_search.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class FilmsFragment: Fragment() {
    private lateinit var listener: FilmsFragmentListener

    private fun showSearch(): Boolean = listener.hasToShowSearch(this)

    private val list: RecyclerView by lazy {
        filmsList.addItemDecoration(FilmsOffsetDecorator())
        return@lazy filmsList
    }

    private val adapter = FilmsAdapter { film ->
        listener.didRequestedToShow(this, film)
    }

    private lateinit var films: LiveData<PagedList<Film>>

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if ( context is FilmsFragmentListener ) {
            listener = context
        } else {
            throw IllegalArgumentException("The attached context does not implement ${FilmsFragmentListener::class.java.canonicalName}")
        }
    }

    // Only perform the inflate on this method!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_films, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress()
        createObserver()

        list.adapter = adapter
        layoutSearch.visibility = if (showSearch()) View.VISIBLE else View.GONE

        searchButton.setOnClickListener { reload(searchText.text.toString()) }
        searchText.addTextChangedListener(FilmicaTextWatcher{ reload(searchText.text.toString()) })
    }

    private fun createObserver() {
        val config = PagedList.Config.Builder().setPageSize(20).build()
        val factory = FilmsDataSourceFactory(listener.getRepositoryFor(this), context!!)
        films = LivePagedListBuilder(factory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build()
        val observer = FilmsLiveDataObserver { pagedList ->
            if (pagedList != null && !pagedList.isEmpty()) {
                adapter.submitList(pagedList)
                showList()
            } else {
                showEmpty()
            }
        }
        films.observe(this, observer)
    }


    private fun reload(text: String) {
        if (text.count() > 2) {
            showProgress()
            (listener.getRepositoryFor(this) as? SearchRepository)?.searchedText = text
            films.removeObservers(this)
            createObserver()
        } else {
            showEmpty()
        }
    }

    private fun showList() {
        filmsList.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        errorView.visibility = View.INVISIBLE
        emptyView.visibility = View.INVISIBLE
    }

    private fun showError() {
        progressBar.visibility = View.INVISIBLE
        filmsList.visibility = View.INVISIBLE
        errorView.visibility = View.VISIBLE
        emptyView.visibility = View.INVISIBLE
    }

    private fun showProgress() {
        filmsList.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        errorView.visibility = View.INVISIBLE
        emptyView.visibility = View.INVISIBLE
    }

    private fun showEmpty() {
        filmsList.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
        errorView.visibility = View.INVISIBLE
        emptyView.visibility = View.VISIBLE
    }
}