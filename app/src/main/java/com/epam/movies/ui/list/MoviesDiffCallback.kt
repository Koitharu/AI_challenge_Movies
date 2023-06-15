package com.epam.movies.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.epam.movies.domain.Movie

class MoviesDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}