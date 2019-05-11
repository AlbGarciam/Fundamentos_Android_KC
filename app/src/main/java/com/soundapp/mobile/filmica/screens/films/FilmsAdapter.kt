package com.soundapp.mobile.filmica.screens.films

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.Film
import com.soundapp.mobile.filmica.screens.utils.TargetFinishedListener
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
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
                        loadImage(value)
                    }
                }


            }

        private fun loadImage(value: Film) {
            // Avoid to lose the reference to target
            val target = TargetFinishedListener { bitmap ->
                setColorFrom(bitmap)
            }

            itemView.cover.tag = target

            Picasso.with(itemView.context)
                    .load(value.coverURL())
                    .error(R.drawable.placeholder)
                    .into(target)
        }

        private fun setColorFrom(bitmap: Bitmap) {
            Palette.from(bitmap).generate { palette ->
                val defaultColor = ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
                val color = swatch?.rgb ?: defaultColor
                itemView.titleContainer.setBackgroundColor(color)
                itemView.cardContainer.setBackgroundColor(color)
                itemView.cover.setImageBitmap(bitmap)
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