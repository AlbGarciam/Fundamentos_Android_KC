package com.soundapp.mobile.filmica.screens.films

import com.soundapp.mobile.filmica.repository.domain.film.Film
import com.soundapp.mobile.filmica.repository.paging.datasourcerepositories.DataSourceRepository

interface FilmsFragmentListener {
    fun didRequestedToShow(fragment: FilmsFragment, film: Film)
    fun getRepositoryFor(fragment: FilmsFragment): DataSourceRepository<Film>
    fun hasToShowSearch(fragment: FilmsFragment): Boolean
}