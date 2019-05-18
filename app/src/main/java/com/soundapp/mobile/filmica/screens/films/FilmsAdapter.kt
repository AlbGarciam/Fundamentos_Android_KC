package com.soundapp.mobile.filmica.screens.films

import android.graphics.Bitmap
import android.view.View
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.screens.utils.TargetFinishedListener
import com.soundapp.mobile.filmica.screens.utils.recyclerview.BaseFilmAdapter
import com.soundapp.mobile.filmica.screens.utils.recyclerview.BaseFilmHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_film.view.*

class FilmsAdapter(val listener: (Film) -> Unit)
    : BaseFilmAdapter<FilmsAdapter.FilmVH>(view = R.layout.item_film, holderCreator = { FilmVH(it, listener) }) {

    class FilmVH(itemView: View, onClick: (Film) -> Unit) : BaseFilmHolder(itemView, onClick) {
        override fun bindFilm(film: Film) {
            super.bindFilm(film)
            with(itemView) {
                labelTitle.text = film.title
                labelGenre.text = film.genre
                labelReview.text = "${film.score}"
                loadImage(film)
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
    }
}