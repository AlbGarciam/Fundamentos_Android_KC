package com.soundapp.mobile.filmica.screens.utils.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.repository.domain.film.Film

open class PagedFilmAdapter<Holder : BaseFilmHolder> (
        @LayoutRes val view: Int,
        val holderCreator: (View) -> Holder,
        diffUtil: DiffUtil.ItemCallback<Film>
): PagedListAdapter<Film, Holder>(diffUtil) {

    protected val films = mutableListOf<Film>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(this.view, parent, false)
        return holderCreator.invoke(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val film = getItem(position) ?: return
        holder.bindFilm(film)
    }

    fun setFilms(films: List<Film>) {
        this.films.clear()
        this.films.addAll(films)
        notifyDataSetChanged()
    }

    fun deleteFilm(position: Int) {
        this.films.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insertFilm(position: Int, film: Film){
        films.add(position, film)
        notifyItemInserted(position)
    }
}