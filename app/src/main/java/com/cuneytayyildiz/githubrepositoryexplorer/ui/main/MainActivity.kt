package com.cuneytayyildiz.githubrepositoryexplorer.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cuneytayyildiz.githubrepositoryexplorer.GithubExplorerApp
import com.cuneytayyildiz.githubrepositoryexplorer.R
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.Resource
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.Status
import com.cuneytayyildiz.githubrepositoryexplorer.ui.details.RepositoryDetailsActivity
import com.cuneytayyildiz.githubrepositoryexplorer.ui.details.RepositoryDetailsActivity.Companion.REQUEST_CODE
import com.cuneytayyildiz.githubrepositoryexplorer.utils.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainListAdapter

    private var canLoadMore = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GithubExplorerApp.appComponent.inject(this)

        initViews()

        initViewModel()
    }

    private fun initViewModel() {
        if (isNetworkStatusAvailable()) {
            mainViewModel = createViewModel(mainViewModelFactory) {
                setCurrentPage(1)

                observe(repos) {
                    renderList(it)
                }
            }
        } else {
            swipeRefreshLayout.snack(getString(R.string.no_connection)) {
                initViewModel()
                it.dismiss()
            }
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recycler_view)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.isEnabled = false

        recyclerView.apply {
            addOnScrollListener(object : InfiniteScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    if (canLoadMore) {
                        mainViewModel.setCurrentPage(page)
                    }
                }
            })
        }
    }

    private fun renderList(result: Resource<MutableList<RepositoryModel>>?) {
        result?.let {
            when (it.status) {
                Status.LOADING -> swipeRefreshLayout.showLoading()
                Status.SUCCESS -> {
                    swipeRefreshLayout.hideLoading()

                    it.data?.let { data ->
                        if (data.isNotEmpty()) {
                            canLoadMore = true
                            if (!::adapter.isInitialized) {
                                adapter = MainListAdapter(data) { model, position ->
                                    RepositoryDetailsActivity.start(this@MainActivity, model, position)
                                }
                                recyclerView.adapter = adapter

                            } else {
                                adapter.addItems(data)
                            }
                        } else {
                            canLoadMore = false
                        }

                    } ?: otherwise { canLoadMore = false }
                }
                Status.ERROR -> {
                    swipeRefreshLayout.hideLoading()
                    swipeRefreshLayout.snack(it.message ?: getString(R.string.error_message)) { snackbar ->
                        renderList(result)
                        snackbar.dismiss()
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    it.extras?.let { extras ->
                        val position = extras.getInt(RepositoryDetailsActivity.REPOSITORY_POSITION)

                        adapter.items[position].isBookmarked =
                                extras.getBoolean(RepositoryDetailsActivity.REPOSITORY_POSITION_BOOKMARKED)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
