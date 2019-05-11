package com.soundapp.mobile.filmica.repository.domain

import org.json.JSONArray
import org.json.JSONObject

data class Film(
        val id: String,
        val title: String = "No title",
        val released: String = "2019-03-05",
        val genre: String = "No genre",
        val score: Float = 0.0f,
        val overview: String = "No overview",
        val coverId: String = ""
) {

    fun coverURL() = "https://image.tmdb.org/t/p/w500/$coverId"

    companion object {
        fun parseFilm(jsonFilm: JSONObject): Film {
            with(jsonFilm) {
                return Film(id = getInt("id").toString(),
                        title = getString("title"),
                        overview = getString("overview"),
                        score = getDouble("vote_average").toFloat(),
                        released = getString("release_date"),
                        genre = parseGenres(getJSONArray("genre_ids")),
                        coverId = optString("poster_path", "")
                )
            }
        }

        fun parseFilms(jsonArray: JSONArray): List<Film> {
            var films = mutableListOf<Film>()
            for (i in 0 until (jsonArray.length() - 1)) {
                films.add(Film.parseFilm(jsonArray.getJSONObject(i)))
            }
            return films.toList()
        }

        private fun parseGenres(jsonArray: JSONArray): String {
            val genres = mutableListOf<String>()
            for (i in 0 until jsonArray.length()-1) {
                val genreInt = jsonArray.getInt(i)
                ApiConstants.genres[genreInt]?.let { genre ->
                    genres.add(genre)
                }
            }
            return genres.joinToString(", ")
        }
    }
}