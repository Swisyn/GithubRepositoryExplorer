package com.cuneytayyildiz.githubrepositoryexplorer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cuneytayyildiz.githubrepositoryexplorer.R
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.local.GithubDb
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

inline fun <reified T : ViewModel> FragmentActivity.createViewModel(
    factory: ViewModelProvider.Factory,
    body: T.() -> Unit
): T {
    val viewModel = ViewModelProviders.of(this, factory).get(T::class.java)
    viewModel.body()
    return viewModel
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

inline fun <reified T : ViewModel> Fragment.createViewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    activity?.let {
        return it.createViewModel(factory, body)
    }
}

fun ImageView.setFavouriteIcon(isFavourite: Boolean?) {
    val resId =
        if (isFavourite == true) R.drawable.ic_favorite_enabled_white_32dp else R.drawable.ic_favorite_disabled_white_32dp
    setImageResource(resId)
}


fun Context.isNetworkStatusAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    return connectivityManager?.activeNetworkInfo?.isConnected ?: false
}


fun Fragment.isNetworkStatusAvailable(): Boolean {
    activity?.let {
        return (it as AppCompatActivity).isNetworkStatusAvailable()
    }
}

fun ImageView.load(avatarUrl: String?) {
    avatarUrl?.let {
        if (it.isNotEmpty()) {
            Picasso.get().load(it).fit().transform(CircleTransformation()).placeholder(R.drawable.bg_placeholder)
                .into(this)
        }
    }
}

fun Int.formatNumber(): String {
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.snack(text: String, retry: (Snackbar) -> Unit) {
    val snackbar = Snackbar.make(this, text, Snackbar.LENGTH_INDEFINITE)
    snackbar.setAction("Retry") { retry(snackbar) }
    snackbar.show()
}


fun SwipeRefreshLayout.showLoading() {
    if (!isRefreshing) {
        post { isRefreshing = true }
    }
}

fun SwipeRefreshLayout.hideLoading() {
    if (isRefreshing) {
        post { isRefreshing = false }
    }
}

fun <T, R> T.otherwise(block: T.() -> R) = run(block)

fun GithubDb.doTransaction(block: (GithubDb) -> Unit) {
    try {
        beginTransaction()
        block(this)
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}