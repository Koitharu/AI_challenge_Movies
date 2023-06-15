package com.epam.movies.data.network

import com.epam.movies.domain.MovieDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MovieDetailsDto(
    @Json(name = "image") val image: String,
    @Json(name = "meta") val meta: String,
    @Json(name = "name") val name: String,
    @Json(name = "price") val price: Int,
    @Json(name = "rating") val rating: String,
    @Json(name = "synopsis") val synopsis: String
) {

    fun toMovieDetails(id: Int) = MovieDetails(
        id = id,
        image = image,
        meta = meta,
        name = name,
        price = price,
        rating = rating,
        synopsis = synopsis
    )
}
