package com.epam.movies.ui.list.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.epam.movies.R
import com.epam.movies.databinding.FragmentFilterBinding
import com.epam.movies.domain.SortField
import com.epam.movies.ui.list.ListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.util.Locale

class FilterBottomSheet : BottomSheetDialogFragment(R.layout.fragment_filter),
    View.OnClickListener, RangeSlider.OnChangeListener {

    private val viewModel by activityViewModels<ListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFilterBinding.bind(view)
        binding.sliderPrice.values = listOf(
            viewModel.priceFrom.value.toFloat(),
            viewModel.priceTo.value.toFloat(),
        )
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        binding.sliderPrice.setLabelFormatter { currencyFormat.format(it.toLong()) }
        val orderBy = viewModel.orderBy.value
        binding.buttonOrderName.isChecked = orderBy == SortField.NAME
        binding.buttonOrderPrice.isChecked = orderBy == SortField.PRICE
        binding.buttonOrderPrice.setOnClickListener(this)
        binding.buttonOrderName.setOnClickListener(this)
        binding.sliderPrice.addOnChangeListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_order_name -> viewModel.setOrderBy(SortField.NAME)
            R.id.button_order_price -> viewModel.setOrderBy(SortField.PRICE)
        }
    }

    override fun onValueChange(slider: RangeSlider, value: Float, fromUser: Boolean) {
        val (from, to) = slider.values
        viewModel.setPriceFilter(from.toInt(), to.toInt())
    }
}