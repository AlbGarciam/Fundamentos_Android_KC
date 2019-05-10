package com.soundapp.mobile.filmica.screens.films

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.Film
import com.soundapp.mobile.filmica.screens.details.DetailActivity
import com.soundapp.mobile.filmica.screens.details.DetailFragment
import kotlinx.android.synthetic.main.activity_films.*

class FilmsActivity: AppCompatActivity(), FilmsFragment.FilmsFragmentListener {

    private fun isDetailAvailable() = detailContainer != null

    // This activity contains two fragments on a tablet and one fragment on a phone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState  == null ) addInitialFragments()
    }

    private fun addInitialFragments() {
        val listFragment = FilmsFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.listContainer, listFragment)
                .commit()
    }

    override fun didRequestedToShow(fragment: FilmsFragment, film: Film) {
        if (isDetailAvailable()) updateDetailWith(film) else presentDetailWith(film)
    }

    private fun updateDetailWith(film: Film) {
        val fragment = DetailFragment.create(film.id)
        supportFragmentManager.beginTransaction()
                .replace(R.id.detailContainer, fragment)
                .commit()
    }

    private fun presentDetailWith(film: Film) {
        DetailActivity.create(this, film)
    }

}