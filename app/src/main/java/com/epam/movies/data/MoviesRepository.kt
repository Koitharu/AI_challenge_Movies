package com.epam.movies.data

import com.epam.movies.data.network.MoviesApi
import com.epam.movies.domain.Movie
import com.epam.movies.domain.MovieDetails
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val api: MoviesApi
) {

    suspend fun getMoviesList(): List<Movie> {
        return api.getMoviesList().map { it.toMovie() }
    }

    suspend fun getDetails(id: Int): MovieDetails {
        return api.getDetails(id).toMovieDetails(id)
    }
}
