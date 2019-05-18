package com.soundapp.mobile.filmica.repository.domain

import android.net.Uri
import com.soundapp.mobile.filmica.BuildConfig

object ApiRoutes {
    const val PAGE_PARAM = "page"
    const val QUERY_PARAM = "query"
    const val SORT_PARAM = "sort_by"
    const val INCLUDE_ADULT_PARAM = "include_adult"
    const val LANGUAGE_PARAM = "language"

    private fun getURIBuilder() = Uri.Builder()
            .scheme("https")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendQueryParameter("api_key", BuildConfig.MovieDBKey)

    fun discoverMovies(params: HashMap<String, String>): String {
        return discoverMoviesURL(language = params[LANGUAGE_PARAM] ?: "en-US",
                sortedBy = params[SORT_PARAM] ?: "popularity.desc",
                page = params[PAGE_PARAM]?.toInt() ?: 1,
                includeAdult = params[INCLUDE_ADULT_PARAM] ?: "false")
    }

    fun discoverMoviesURL(language: String = "en-US", sortedBy: String = "popularity.desc", page: Int = 1, includeAdult: String = "false"): String {
        return getURIBuilder()
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sortedBy)
                .appendQueryParameter("include_adult", includeAdult)
                .appendQueryParameter("page", page.toString())
                .build()
                .toString()
    }

    fun trendingMovies(params: HashMap<String, String>): String {
        return trendingMoviesURL(language = params[LANGUAGE_PARAM] ?: "en-US",
                sortedBy = params[SORT_PARAM] ?: "popularity.desc",
                page = params[PAGE_PARAM]?.toInt() ?: 1,
                includeAdult = params[INCLUDE_ADULT_PARAM] ?: "false")
    }

    fun trendingMoviesURL(language: String = "en-US", sortedBy: String = "popularity.desc", page: Int = 1, includeAdult: String = "false"): String {
        return getURIBuilder()
                .appendPath("trending")
                .appendPath("movie")
                .appendPath("day")
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sortedBy)
                .appendQueryParameter("include_adult", includeAdult)
                .appendQueryParameter("page", page.toString())
                .build()
                .toString()
    }

    fun searchMovies(params: HashMap<String, String>): String {
        return searchMoviesURL(
                title = params[QUERY_PARAM] ?: "",
                language = params[LANGUAGE_PARAM] ?: "en-US",
                sortedBy = params[SORT_PARAM] ?: "popularity.desc",
                page = params[PAGE_PARAM]?.toInt() ?: 1,
                includeAdult = params[INCLUDE_ADULT_PARAM] ?: "false")
    }

    fun searchMoviesURL(title: String, language: String = "en-US", sortedBy: String = "popularity.desc", page: Int = 1 , includeAdult: String = "false"): String {
        return getURIBuilder()
                .appendPath("search")
                .appendPath("movie")
                .appendQueryParameter("query", title)
                .appendQueryParameter("language", language)
                .appendQueryParameter("sort_by", sortedBy)
                .appendQueryParameter("include_adult", includeAdult)
                .appendQueryParameter("page", page.toString())
                .build()
                .toString()
    }
}