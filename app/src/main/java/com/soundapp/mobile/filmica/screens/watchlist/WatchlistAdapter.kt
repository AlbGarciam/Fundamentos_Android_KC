package com.soundapp.mobile.filmica.screens.watchlist

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.Film
import com.soundapp.mobile.filmica.screens.utils.TargetFinishedListener
import com.soundapp.mobile.filmica.screens.utils.recyclerview.BaseFilmAdapter
import com.soundapp.mobile.filmica.screens.utils.recyclerview.BaseFilmHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_watchlist.view.*

class WatchlistAdapter(val listener: (Film) -> Unit):
        BaseFilmAdapter<WatchlistAdapter.WatchlistVH>(view = R.layout.item_watchlist,
                holderCreator = { WatchlistVH(it, listener) }) {

    class WatchlistVH(itemView: View, onClick: (Film) -> Unit): BaseFilmHolder(itemView, onClick){
        override fun bindFilm(film: Film) {
            super.bindFilm(film)
            with(itemView) {
                labelTitle.text = film.title
                labelVotes.text = film.score.toString()
                labelOverview.text = film.overview
                loadImage(film)
            }
        }

        private fun loadImage(value: Film) {
            // Avoid to lose the reference to target
            val target = TargetFinishedListener { bitmap ->
                setColorFrom(bitmap)
            }

            itemView.imgPoster.tag = target

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
                val overlayColor = Color.argb(
                        (Color.alpha(color)*0.5).toInt(),
                        Color.red(color),
                        Color.green(color),
                        Color.blue(color))

                itemView.imgOverlay.setBackgroundColor(overlayColor)
                itemView.imgPoster.setImageBitmap(bitmap)
            }
        }
    }
}