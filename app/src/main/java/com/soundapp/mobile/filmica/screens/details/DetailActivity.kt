package com.soundapp.mobile.filmica.screens.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.repository.domain.film.Film

class DetailActivity : AppCompatActivity() {
    companion object {
        fun create(context: Context?, film: Film, isHighlighted: Boolean) {
            val bundle = Bundle()
            val intent = Intent(context, DetailActivity::class.java)
            bundle.putSerializable(FILM_EXTRA, film)
            bundle.putBoolean(LOCAL_FILM_EXTRA, isHighlighted)
            intent.putExtras(bundle)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Associate te ID of the file to the view

        if (savedInstanceState  == null ) {
            val film = intent.getSerializableExtra(FILM_EXTRA) as Film
            val isHighlighted = intent.getBooleanExtra(LOCAL_FILM_EXTRA, false)
            val fragment = DetailFragment.create(film, isHighlighted)
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }
    }

}
