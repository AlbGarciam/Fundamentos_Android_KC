package com.soundapp.mobile.filmica.screens.films

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.Film
import kotlinx.android.synthetic.main.fragment_films.*
import java.lang.IllegalArgumentException

class FilmsFragment: Fragment() {
    lateinit var listener: FilmsFragmentListener

    private val list: RecyclerView by lazy {
        filmsList.layoutManager = LinearLayoutManager(context) // AppCompatActivity inherits from Context
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
        adapter.setFilms(FilmsRepo.films)
    }

    interface FilmsFragmentListener {
        fun didRequestedToShow(fragment: FilmsFragment, film: Film)
    }
}