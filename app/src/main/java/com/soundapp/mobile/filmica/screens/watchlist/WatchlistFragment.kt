package com.soundapp.mobile.filmica.screens.watchlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.Film
import kotlinx.android.synthetic.main.fragment_watchlist.*

class WatchlistFragment : Fragment() {

    val adapter: WatchlistAdapter = WatchlistAdapter {film ->
        showDetail(film)
    }

    private fun showDetail(film: Film){

    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchlistList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        FilmsRepo.getStoredFilms(context!!) {
            adapter.setFilms(it)
        }
    }


}
