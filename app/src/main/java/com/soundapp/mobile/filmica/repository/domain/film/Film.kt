package com.soundapp.mobile.filmica.repository.domain.film

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.soundapp.mobile.filmica.repository.domain.ApiConstants
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

@Entity
data class Film(
        @PrimaryKey val id: String = UUID.randomUUID().toString(),
        val title: String = "No title",
        val released: String = "2019-03-05",
        val genre: String = "No genre",
        val score: Float = 0.0f, // By default the column name is the name of the variable
        val overview: String = "No overview",
        @ColumnInfo(name = "cover_id") val coverId: String = ""
) {
    @Ignore
    constructor(): this("") // Ignore empty constructor

    fun coverURL() = "https://image.tmdb.org/t/p/w500/$coverId"

    companion object {
        private fun parseFilm(jsonFilm: JSONObject): Film {
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
            val films = mutableListOf<Film>()
            for (i in 0 until (jsonArray.length() - 1)) {
                films.add(parseFilm(jsonArray.getJSONObject(i)))
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