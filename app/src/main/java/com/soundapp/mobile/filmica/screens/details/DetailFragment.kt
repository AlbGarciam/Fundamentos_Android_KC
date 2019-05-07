package com.soundapp.mobile.filmica.screens.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.Film
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
        addButton.setOnClickListener {
            Toast.makeText(this@DetailFragment.context, "Button tapped", Toast.LENGTH_SHORT).show()
        }
        arguments?.getString(PARAMS.ID.value, "")?.let { filmId ->
            FilmsRepo.findFilmBy(filmId)?.let {film ->
                titleLabel.text = film.title
                descriptionLabel.text = film.overview
                genresLabel.text = film.genre
                ratingsLabel.text = "${film.score}"
            }
        }
    }
}