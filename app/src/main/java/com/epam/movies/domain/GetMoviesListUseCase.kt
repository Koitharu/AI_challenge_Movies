package com.epam.movies.domain

import com.epam.movies.data.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMoviesListUseCase @Inject constructor(
    private val repository: MoviesRepository,
) {

    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        repository.getMoviesList()
    }
}