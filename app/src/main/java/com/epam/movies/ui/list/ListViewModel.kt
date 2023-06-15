package com.epam.movies.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epam.movies.domain.GetMoviesListUseCase
import com.epam.movies.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    init {
        loadList()
    }

    private fun loadList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _list.value = useCase()
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