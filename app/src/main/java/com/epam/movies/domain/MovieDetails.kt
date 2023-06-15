package com.epam.movies.domain

data class MovieDetails(
    val id: Int,
    val image: String,
    val meta: String,
    val name: String,
    val price: Int,
    val rating: String,
    val synopsis: String
)
