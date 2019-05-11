package com.soundapp.mobile.filmica.screens.details

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.Film
import com.soundapp.mobile.filmica.screens.utils.TargetFinishedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_film.view.*

class DetailFragment: Fragment() {
    companion object {
        private enum class PARAMS(val value: String) {
            ID("id")
        }

        fun create(film: Film) : Fragment {
            return create(filmId = film.id)
        }

        fun create(filmId: String) : Fragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putString(PARAMS.ID.value, filmId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonAdd.setOnClickListener {
            Toast.makeText(this@DetailFragment.context, "Button tapped", Toast.LENGTH_SHORT).show()
        }
        arguments?.getString(PARAMS.ID.value, "")?.let { filmId ->
            FilmsRepo.findFilmBy(filmId)?.let {film ->
                textTitle.text = film.title
                textRating.text = "${film.score}"
                labelDescription.text = film.overview
                labelGenres.text = film.genre
                labelDate.text = film.released
                loadImage(film)
            }
        }
    }

    private fun loadImage(value: Film) {
        // Avoid to lose the reference to target
        val target = TargetFinishedListener { bitmap ->
            setColorFrom(bitmap)
        }

        imgFilm.tag = target

        Picasso.with(context)
                .load(value.coverURL())
                .error(R.drawable.placeholder)
                .into(target)
    }

    private fun setColorFrom(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            // This code cannot be executed if it is not on an activity
            val defaultColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
            val swatch = palette?.vibrantSwatch ?: palette?.dominantSwatch
            val color = swatch?.rgb ?: defaultColor
            imgFilm.setImageBitmap(bitmap)
            val overlayColor = Color.argb(
                    (Color.alpha(color)*0.5).toInt(),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color))
            overlay.setBackgroundColor(overlayColor)
            buttonAdd.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}