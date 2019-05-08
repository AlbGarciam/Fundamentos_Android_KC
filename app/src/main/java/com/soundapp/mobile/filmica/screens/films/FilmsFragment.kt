package com.soundapp.mobile.filmica.screens.films

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.screens.details.DetailActivity
import kotlinx.android.synthetic.main.fragment_films.*

class FilmsFragment: Fragment() {
    private val list: RecyclerView by lazy {
        filmsList.layoutManager = LinearLayoutManager(context) // AppCompatActivity inherits from Context
        return@lazy filmsList
    }

    private val adapter = FilmsAdapter { film ->
        DetailActivity.create(this@FilmsFragment.context, film)
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
}