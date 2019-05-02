package com.soundapp.mobile.filmica.Repository.Domain

data class Film(
        val title: String = "No title",
        val released: String = "2019-03-05",
        val genre: String = "No genre",
        val score: Float = 0.0f,
        val overview: String = "No overview"
)