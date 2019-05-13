package com.soundapp.mobile.filmica.screens.utils.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.repository.domain.Film

open class BaseFilmAdapter<Holder : BaseFilmHolder> (
        @LayoutRes val view: Int,
        val holderCreator: (View) -> Holder
) : RecyclerView.Adapter<Holder>() {

    protected val films = mutableListOf<Film>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(this.view, parent, false)
        return holderCreator.invoke(view)
    }

    override fun getItemCount(): Int {
        return films.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val film = films[position]
        holder.bindFilm(film)
    }

    fun setFilms(films: List<Film>) {
        this.films.clear()
        this.films.addAll(films)
        notifyDataSetChanged()
    }
}