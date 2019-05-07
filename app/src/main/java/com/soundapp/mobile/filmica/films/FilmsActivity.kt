package com.soundapp.mobile.filmica.films

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.soundapp.mobile.filmica.DetailActivity
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.Repository.Domain.Film
import com.soundapp.mobile.filmica.Repository.FilmsRepo

class FilmsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        val list: RecyclerView = findViewById(R.id.list)
        val adapter = FilmsAdapter { film ->
            this@FilmsActivity.launchFilmDetail(film)
        }

        list.layoutManager = LinearLayoutManager(this) // AppCompatActivity inherits from Context
        list.adapter = adapter

        adapter.setFilms(FilmsRepo.films)
    }

    private fun launchFilmDetail(film: Film) {
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}