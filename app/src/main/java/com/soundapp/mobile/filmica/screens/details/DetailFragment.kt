package com.soundapp.mobile.filmica.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.Film
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*

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
                Picasso.with(context)
                        .load(film.coverURL())
                        .into(imgFilm)
            }
        }
    }
}