package com.epam.movies.ui

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> AsyncListDifferDelegationAdapter<T>.setItemsAwait(items: List<T>) {
    suspendCoroutine { cont ->
        setItems(items) {
            cont.resume(Unit)
        }
    }
}