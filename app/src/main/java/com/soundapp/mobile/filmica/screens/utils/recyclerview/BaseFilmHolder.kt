package com.soundapp.mobile.filmica.screens.utils.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.repository.domain.Film

open class BaseFilmHolder(itemView: View,
                          clickListener: ((Film) -> Unit)? = null ) : RecyclerView.ViewHolder(itemView) {
    lateinit var film: Film

    init {
        itemView.setOnClickListener {
            clickListener?.invoke(film)
        }
    }

    open fun bindFilm(film:Film) {
        this.film = film
    }
}