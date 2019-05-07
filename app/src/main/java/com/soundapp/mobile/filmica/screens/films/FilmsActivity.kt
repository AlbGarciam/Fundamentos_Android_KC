package com.soundapp.mobile.filmica.screens.films

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.soundapp.mobile.filmica.R

class FilmsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        if (savedInstanceState  == null ) {
            val listFragment = FilmsFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, listFragment)
                    .commit()
        }
    }

}