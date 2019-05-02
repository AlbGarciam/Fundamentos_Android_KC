package com.soundapp.mobile.filmica.Repository

import com.soundapp.mobile.filmica.Repository.Domain.Film

// Let's create a singleton on the app
object FilmsRepo {
    val films : MutableList<Film> = mutableListOf() // Can be also declared as val films = mutableListOf<Film>()
        get() {// In order to avoid recursive calls, we must use field instead of films
            if (field.isEmpty())
                field.addAll(dummyFilms())
            return field
        }

    private fun dummyFilms() : MutableList<Film> {
        return (0..10).map { i ->  Film(title = "film $i", overview = "Overview $i") }.toMutableList()
    }
}