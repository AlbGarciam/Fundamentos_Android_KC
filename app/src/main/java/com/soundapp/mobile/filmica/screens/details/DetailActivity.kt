package com.soundapp.mobile.filmica.screens.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.film.Film

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
        val filmId = intent.getStringExtra(Extras.ID.value)
        if (savedInstanceState  == null ) {
            val fragment = DetailFragment.create(filmId)
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }
    }

}
