package com.soundapp.mobile.filmica.screens.utils

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.soundapp.mobile.filmica.repository.domain.film.Film

class FilmsLiveDataObserver(val listener: (PagedList<Film>?) -> Unit): Observer<PagedList<Film>> {
    /**
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: PagedList<Film>?) {
        listener.invoke(t)
    }
}