package com.soundapp.mobile.filmica.screens.films

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.Film
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_film.view.*

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

    fun setFilms(list: List<Film>) {
        films.clear()
        films.addAll(list)
        notifyDataSetChanged() // Only when the entire set changed
    }

    inner class FilmVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var film: Film? = null
            set(value) {
                field = value
                value?.let { value ->
                    with(itemView) {
                        labelTitle.text = value.title
                        labelGenre.text = value.genre
                        labelReview.text = "${value.score}"
                        Picasso.with(itemView.context)
                                .load(value.coverURL())
                                .into(cover)
                    }
                }


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