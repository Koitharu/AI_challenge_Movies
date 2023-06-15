package com.epam.movies.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("/movies")
    suspend fun getMoviesList(): List<MovieDto>

    @GET("/movieDetails")
    suspend fun getDetails(
        @Query("id") id: Int
    ): MovieDetailsDto
}
