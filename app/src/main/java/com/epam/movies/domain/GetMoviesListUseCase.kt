package com.epam.movies.domain

import com.epam.movies.data.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMoviesListUseCase @Inject constructor(
    private val repository: MoviesRepository,
) {

    private var cachedList: List<Movie>? = null
    private val cacheMutex = Mutex()

    suspend operator fun invoke(
        orderBy: SortField,
        price: IntRange,
    ) = withContext(Dispatchers.Default) {
        val list = getList().toMutableList()
        when (orderBy) {
            SortField.NAME -> list.sortBy { it.name }
            SortField.PRICE -> list.sortBy { it.price }
        }
        list.retainAll { it.price in price }
        list
    }

    private suspend fun getList() = cacheMutex.withLock {
        cachedList?.let {
            return@withLock it
        }
        repository.getMoviesList().also { cachedList = it }
    }
}