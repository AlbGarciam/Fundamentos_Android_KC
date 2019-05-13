package com.soundapp.mobile.filmica.repository.domain

import androidx.room.*

@Dao
interface FilmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilm(film: Film)

    @Query("SELECT * FROM film")
    fun getFilms() : List<Film>

    @Delete
    fun deleteFilm(film: Film)
}