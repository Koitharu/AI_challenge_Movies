package com.epam.movies.ui.list

import com.epam.movies.databinding.ItemMovieBinding
import com.epam.movies.domain.Movie
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import java.text.NumberFormat
import java.util.Locale

fun movieItemAD(
    onClick: (Movie) -> Unit,
) = adapterDelegateViewBinding<Movie, Movie, ItemMovieBinding>(
    { layoutInflater, parent -> ItemMovieBinding.inflate(layoutInflater, parent, false) }
) {

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    itemView.setOnClickListener { onClick(item) }

    bind {
        binding.textViewTitle.text = item.name
        binding.textViewPrice.text = currencyFormat.format(item.price)
    }
}