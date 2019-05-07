package com.soundapp.mobile.filmica.screens.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.FilmsRepo
import com.soundapp.mobile.filmica.repository.domain.Film
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        private enum class Extras(val value: String) {
            ID("id")
        }

        fun create(context: Context?, film: Film) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Companion.Extras.ID.value, film.id)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Associate te ID of the file to the view
        addButton.setOnClickListener {
            Toast.makeText(this@DetailActivity, "Button tapped", Toast.LENGTH_SHORT).show()
        }
        val film = FilmsRepo.findFilmBy(intent.getStringExtra(Companion.Extras.ID.value))
        film?.let { film ->
            titleLabel.text = film.title
            descriptionLabel.text = film.overview
            genresLabel.text = film.genre
            ratingsLabel.text = "${film.score}"
        }
    }

}
