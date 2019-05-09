package com.soundapp.mobile.filmica.repository.domain

import org.json.JSONObject

data class Film(
        val id: String,
        val title: String = "No title",
        val released: String = "2019-03-05",
        val genre: String = "No genre",
        val score: Float = 0.0f,
        val overview: String = "No overview"
) {
    companion object {
        fun parseFilm(jsonFilm: JSONObject): Film {
            with(jsonFilm) {
                return Film(id = getInt("id").toString(),
                        title = getString("title"),
                        overview = getString("overview"),
                        score = getDouble("vote_average").toFloat(),
                        released = getString("release_date")
                )
            }
        }
    }
}