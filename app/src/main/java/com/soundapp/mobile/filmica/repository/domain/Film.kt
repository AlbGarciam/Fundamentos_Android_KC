package com.soundapp.mobile.filmica.repository.domain

data class Film(
        val id: String,
        val title: String = "No title",
        val released: String = "2019-03-05",
        val genre: String = "No genre",
        val score: Float = 0.0f,
        val overview: String = "No overview"
)