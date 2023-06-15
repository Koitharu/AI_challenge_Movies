package com.epam.movies.ui.list

import com.epam.movies.databinding.ItemMovieBinding
import com.epam.movies.domain.Movie
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun movieItemAD(
    onClick: (Movie) -> Unit,
) = adapterDelegateViewBinding<Movie, Movie, ItemMovieBinding>(
    { layoutInflater, parent -> ItemMovieBinding.inflate(layoutInflater, parent, false) }
) {


}