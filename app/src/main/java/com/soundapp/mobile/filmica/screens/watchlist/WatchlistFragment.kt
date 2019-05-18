package com.soundapp.mobile.filmica.screens.watchlist


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.screens.utils.SwipeToDeleteCallback
import com.soundapp.mobile.filmica.screens.utils.recyclerview.BaseFilmHolder
import kotlinx.android.synthetic.main.fragment_watchlist.*

class WatchlistFragment : Fragment() {
    interface WatchlistFragmentListener {
        fun didRequestedToShow(fragment: WatchlistFragment, film: Film)
    }

    private lateinit var listener: WatchlistFragmentListener

    private val adapter: WatchlistAdapter = WatchlistAdapter {film ->
        listener.didRequestedToShow(this, film)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if ( context is WatchlistFragmentListener ) {
            listener = context
        } else {
            throw IllegalArgumentException("The attached context does not implement ${WatchlistFragmentListener::class.java.canonicalName}")
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeHandler()
        watchlistList.adapter = adapter
    }

    private fun deleteFilm(film: Film, position: Int) {
        FilmsRepo.removeFilm(context!!, film) {
            adapter.deleteFilm(position)
            Snackbar.make(watchlistList, R.string.add_snackbar, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo){
                        FilmsRepo.saveFilm(this@WatchlistFragment.context!!, film) {
                            adapter.insertFilm(position, film)
                        }
                    }.setActionTextColor(Color.CYAN)
                    .show()
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            FilmsRepo.getStoredFilms(context!!) {
                adapter.setFilms(it)
            }
    }

    private fun setupSwipeHandler() {
        val swipeHandler = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val film = (viewHolder as? BaseFilmHolder)?.film
                val position = viewHolder.adapterPosition
                film?.let { deleteFilm(it, position) }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(watchlistList)
    }

    override fun onResume() {
        super.onResume()
        FilmsRepo.getStoredFilms(context!!) {
            adapter.setFilms(it)
        }
    }


}
