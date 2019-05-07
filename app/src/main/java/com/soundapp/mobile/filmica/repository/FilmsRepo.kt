package com.soundapp.mobile.filmica.repository

import com.soundapp.mobile.filmica.repository.domain.Film

// Let's create a singleton on the app
object FilmsRepo {
    val films : MutableList<Film> = mutableListOf() // Can be also declared as val films = mutableListOf<Film>()
        get() {// In order to avoid recursive calls, we must use field instead of films
            if (field.isEmpty())
                field.addAll(dummyFilms())
            return field
        }

    fun findFilmBy(id: String): Film? {
        return films.find { it.id == id }
    }

    private fun dummyFilms() : MutableList<Film> {
        return (0..10).map { i ->  Film(id = "$i", title = "film $i", overview = "Overview $i") }.toMutableList()
    }
}