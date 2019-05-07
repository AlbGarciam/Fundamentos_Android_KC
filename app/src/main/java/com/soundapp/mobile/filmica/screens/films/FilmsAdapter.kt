package com.soundapp.mobile.filmica.screens.films

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.Film

class FilmsAdapter(val listener: (Film) -> Unit) : RecyclerView.Adapter<FilmsAdapter.FilmVH>() {
    private val films: MutableList<Film> = mutableListOf()

    override fun onCreateViewHolder(recyclerView: ViewGroup, type: Int): FilmVH {
        val view = LayoutInflater.from(recyclerView.context)
                .inflate(R.layout.item_film, recyclerView, false)
        return FilmVH(view)
    }

    override fun onBindViewHolder(viewHolder: FilmVH, position: Int) {
        val film = films[position]
        viewHolder.film = film
    }

    // Returns the number of items on the recycler view
    override fun getItemCount(): Int {
        return films.size
    }

    fun setFilms(list: MutableList<Film>) {
        films.clear()
        films.addAll(list)
        notifyDataSetChanged() // Only when the entire set changed
    }

    inner class FilmVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var film: Film? = null
            set(value) {
                field = value
                // In case that value is null the following line won't be executed
                (itemView as? TextView)?.text = value?.title
            }

        init {
            itemView.setOnClickListener {
                film?.let { film ->
                    listener.invoke(film)
                }
            }
        }
    }
}