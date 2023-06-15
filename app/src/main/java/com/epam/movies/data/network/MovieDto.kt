package com.epam.movies.data.network

import com.epam.movies.domain.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MovieDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "price") val price: Int
) {

    fun toMovie() = Movie(
        id = id.toInt(),
        name = name,
        price = price
    )
}
