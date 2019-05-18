package com.soundapp.mobile.filmica.repository.domain.film

import androidx.recyclerview.widget.DiffUtil

class FilmsDiffutil: DiffUtil.ItemCallback<Film>() {
    override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
        return oldItem.id == newItem.id
    }
}