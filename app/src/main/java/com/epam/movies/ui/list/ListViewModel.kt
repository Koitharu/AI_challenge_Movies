package com.epam.movies.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epam.movies.domain.GetMoviesListUseCase
import com.epam.movies.domain.Movie
import com.epam.movies.domain.SortField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val useCase: GetMoviesListUseCase,
) : ViewModel() {

    private val _list = MutableStateFlow<List<Movie>>(emptyList())
    val list get() = _list.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _onError = MutableSharedFlow<Exception>()
    val onError get() = _onError.asSharedFlow()

    private val _orderBy = MutableStateFlow(SortField.NAME)
    val orderBy get() = _orderBy.asStateFlow()

    private val _priceFrom = MutableStateFlow(0)
    val priceFrom get() = _priceFrom.asStateFlow()

    private val _priceTo = MutableStateFlow(1500)
    val priceTo get() = _priceTo.asStateFlow()

    private var loadingJob: Job? = null

    init {
        combine(_orderBy, _priceFrom, _priceTo, ::loadList)
            .launchIn(viewModelScope)
    }

    fun setOrderBy(order: SortField) {
        _orderBy.value = order
    }

    fun setPriceFilter(from: Int, to: Int) {
        _priceFrom.value = from
        _priceTo.value = to
    }

    private fun loadList(sort: SortField, minPrice: Int, maxPrice: Int) {
        val prevJob = loadingJob
        loadingJob = viewModelScope.launch {
            prevJob?.cancelAndJoin()
            _isLoading.value = true
            try {
                _list.value = useCase(sort, minPrice..maxPrice)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _onError.emit(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}