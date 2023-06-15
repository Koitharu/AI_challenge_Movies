package com.epam.movies.ui.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.epam.movies.databinding.ActivityListBinding
import com.epam.movies.ui.setItemsAwait
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityListBinding
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityListBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
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
    }
}
