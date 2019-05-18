package com.soundapp.mobile.filmica.screens.films

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.ApiRoutes.QUERY_PARAM
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.repository.paging.datasourcerepositories.DataSourceRepository
import com.soundapp.mobile.filmica.screens.utils.FilmicaTextWatcher
import com.soundapp.mobile.filmica.screens.utils.FilmsOffsetDecorator
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_search.*


class FilmsFragment(val showSearch: Boolean = false, val repo: DataSourceRepository<Film>): Fragment() {
    interface FilmsFragmentListener {
        fun didRequestedToShow(fragment: FilmsFragment, film: Film)
    }

    private lateinit var listener: FilmsFragmentListener

    private val list: RecyclerView by lazy {
        filmsList.addItemDecoration(FilmsOffsetDecorator())
        return@lazy filmsList
    }

    private val adapter = FilmsAdapter { film ->
        listener.didRequestedToShow(this@FilmsFragment, film)
    }

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
        list.adapter = adapter
        layoutSearch.visibility = if (showSearch) View.VISIBLE else View.GONE

        searchButton.setOnClickListener {
            if (searchText.text.count() > 2)
                reload()
        }

        searchText.addTextChangedListener(FilmicaTextWatcher{ text ->
            if (text.count() < 3) showEmpty() else reload()
        })

        retryButton.setOnClickListener { reload() }
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    private fun reload() {
        if ( !showSearch ) {
            showProgress()
            repo.get(HashMap(), context!!, { setFilms(it) }, { showError() })
        } else if (searchText.text.count() > 2) {
            showProgress()
            val hashMap = hashMapOf<String, String>()
            hashMap[QUERY_PARAM] = searchText.text.toString()
            repo.get(hashMap, context!!, { setFilms(it) }, { showError() })
        } else {
            showEmpty()
        }
    }

    fun setFilms(films: List<Film>) {
        adapter.setFilms(films)
        if (films.count() == 0) showEmpty() else showList()
    }

    private fun showList() {
        filmsList.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
        errorView.visibility = View.INVISIBLE
        emptyView.visibility = View.INVISIBLE
    }

    fun showError() {
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