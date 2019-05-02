package com.soundapp.mobile.filmica

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.soundapp.mobile.filmica.Repository.FilmsRepo

class FilmsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)
        val films = FilmsRepo.films
    }

    fun onFilmsButtonClicked(view: View) {
        val intent: Intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}