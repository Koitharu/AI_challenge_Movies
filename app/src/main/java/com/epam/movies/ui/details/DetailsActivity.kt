package com.epam.movies.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.request.ImageRequest
import com.epam.movies.databinding.ActivityDetailsBinding
import com.epam.movies.domain.MovieDetails
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var currencyFormat: NumberFormat

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

        lifecycleScope.launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.isLoading.collect {
                viewBinding.progressBar.isVisible = it
            }
        }
        lifecycleScope.launch {
            viewModel.details.filterNotNull()
                .collect(::onDetailsChanged)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onError.collect {
                    Snackbar.make(
                        viewBinding.scrollView,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onDetailsChanged(details: MovieDetails) {
        title = details.name
        viewBinding.textViewTitle.text = details.name
        viewBinding.textViewMeta.text = details.meta
        viewBinding.textViewPrice.text = currencyFormat.format(details.price.toLong())
        viewBinding.textViewSynopsis.text = details.synopsis
        val imageRequest = ImageRequest.Builder(this)
            .data(details.image)
            .lifecycle(this)
            .target(viewBinding.imageViewImage)
            .crossfade(true)
            .build()
        imageLoader.enqueue(imageRequest)
    }

    companion object {

        const val EXTRA_ID = "id"

        fun newIntent(context: Context, id: Int) = Intent(context, DetailsActivity::class.java)
            .putExtra(EXTRA_ID, id)
    }
}