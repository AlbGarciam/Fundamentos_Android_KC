package com.soundapp.mobile.filmica.repository.domain

import android.net.Uri
import com.soundapp.mobile.filmica.BuildConfig

object ApiRoutes {
    private fun getURIBuilder() = Uri.Builder()
                .scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", BuildConfig.MovieDBKey)


    fun discoverMoviesURL(language: String = "en-US", sortedBy: String = "popularity.desc"): String {
        return getURIBuilder()
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sortedBy)
                .appendQueryParameter("include_adult", "false")
                .build()
                .toString()
    }

    fun trendingMoviesURL(language: String = "en-US", sortedBy: String = "popularity.desc"): String {
        return getURIBuilder()
                .appendPath("trending")
                .appendPath("movie")
                .appendPath("day")
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sortedBy)
                .appendQueryParameter("include_adult", "false")
                .build()
                .toString()
    }

    fun searchMoviesURL(title: String, language: String = "en-US", sortedBy: String = "popularity.desc"): String {
        return getURIBuilder()
                .appendPath("search")
                .appendPath("movie")
                .appendQueryParameter("query", title)
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sortedBy)
                .appendQueryParameter("include_adult", "false")
                .build()
                .toString()
    }
}