package com.soundapp.mobile.filmica.films

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.soundapp.mobile.filmica.DetailActivity
import com.soundapp.mobile.filmica.R
import com.soundapp.mobile.filmica.Repository.FilmsRepo

class FilmsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        val list: RecyclerView = findViewById(R.id.list)
        val adapter = FilmsAdapter()

        list.layoutManager = LinearLayoutManager(this) // AppCompatActivity inherits from Context
        list.adapter = adapter

        adapter.setFilms(FilmsRepo.films)
    }

    fun onFilmsButtonClicked(view: View) {
        val intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }
}