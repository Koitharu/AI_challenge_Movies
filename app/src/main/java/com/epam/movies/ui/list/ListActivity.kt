package com.epam.movies.ui.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.epam.movies.databinding.ActivityListBinding
import com.epam.movies.ui.setItemsAwait
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityListBinding
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityListBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
        val listAdapter = AsyncListDifferDelegationAdapter(
            MoviesDiffCallback(),
            movieItemAD { }
        )
        viewBinding.recyclerView.adapter = listAdapter
        viewBinding.recyclerView.setHasFixedSize(true)
        viewBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )

        lifecycleScope.launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.isLoading.collect {
                viewBinding.progressBar.isVisible = it
            }
        }
        lifecycleScope.launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.list.collect {
                listAdapter.setItemsAwait(it)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onError.collect {
                    Snackbar.make(
                        viewBinding.recyclerView,
                        it.message.orEmpty(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
