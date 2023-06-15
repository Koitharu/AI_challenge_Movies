package com.epam.movies.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epam.movies.domain.GetMovieDetailsUseCase
import com.epam.movies.domain.MovieDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: GetMovieDetailsUseCase,
) : ViewModel() {

    private val _details = MutableStateFlow<MovieDetails?>(null)
    val details get() = _details.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _onError = MutableSharedFlow<Exception>()
    val onError get() = _onError.asSharedFlow()

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val id = checkNotNull(savedStateHandle.get<Int>(DetailsActivity.EXTRA_ID))
                _details.value = useCase(id)
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