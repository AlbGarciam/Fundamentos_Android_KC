package com.soundapp.mobile.filmica.screens.films

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.Film
import com.soundapp.mobile.filmica.screens.details.DetailActivity
import com.soundapp.mobile.filmica.screens.details.DetailFragment
import com.soundapp.mobile.filmica.screens.watchlist.WatchlistFragment
import kotlinx.android.synthetic.main.activity_films.*

// Compilation constant (Not runtime constant!!!!)
const val FILMS_TAG: String = "Films"
const val WATCHLIST_TAG: String = "Watchlist"

class FilmsActivity: AppCompatActivity(), FilmsFragment.FilmsFragmentListener {

    private fun isDetailAvailable() = detailContainer != null
    private lateinit var activeFragment : Fragment
    private lateinit var filmsFragment: FilmsFragment
    private lateinit var watchlistFragment: WatchlistFragment

    // This activity contains two fragments on a tablet and one fragment on a phone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState == null ) {
            setupFragments()
        } else {
            val currentTag = savedInstanceState.getString("Active_item", FILMS_TAG)
            restoreFragments(currentTag)
        }

        navigationBar.setOnNavigationItemSelectedListener {menuItem ->
            when (menuItem.itemId) {
                R.id.action_discover -> showMainFragment(filmsFragment)
                R.id.action_watchlist -> showMainFragment(watchlistFragment)
            }
            true
        }
    }

    // Executed before destroying the activity
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("Active_item",activeFragment.tag)
    }

    private fun restoreFragments(tag: String) {
        filmsFragment = supportFragmentManager.findFragmentByTag(FILMS_TAG) as FilmsFragment
        watchlistFragment = supportFragmentManager.findFragmentByTag(WATCHLIST_TAG) as WatchlistFragment
        activeFragment = if (tag == FILMS_TAG) filmsFragment else watchlistFragment
    }

    private fun setupFragments() {
        filmsFragment = FilmsFragment()
        watchlistFragment = WatchlistFragment()
        activeFragment = filmsFragment
        supportFragmentManager.beginTransaction()
                .add(R.id.listContainer, filmsFragment, FILMS_TAG)
                .add(R.id.listContainer, watchlistFragment, WATCHLIST_TAG)
                .hide(watchlistFragment)
                .commit()
    }

    private fun showMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit()
        activeFragment = fragment
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