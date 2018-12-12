package com.cuneytayyildiz.githubrepositoryexplorer.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cuneytayyildiz.githubrepositoryexplorer.GithubExplorerApp
import com.cuneytayyildiz.githubrepositoryexplorer.R
import com.cuneytayyildiz.githubrepositoryexplorer.data.RepositoryModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.StargazerModel
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.Resource
import com.cuneytayyildiz.githubrepositoryexplorer.data.source.vo.Status
import com.cuneytayyildiz.githubrepositoryexplorer.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class RepositoryDetailsActivity : AppCompatActivity() {

    private lateinit var fabBookmark: FloatingActionButton
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: RepositoryDetailsViewModelFactory

    private lateinit var adapter: StargazersListAdapter
    private lateinit var repositoryDetailsViewModel: RepositoryDetailsViewModel
    private lateinit var selectedRepository: RepositoryModel

    private var canLoadMore = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        GithubExplorerApp.appComponent.inject(this)

        initViews()

        initViewModel()
    }

    private fun initViewModel() {
        selectedRepository = intent.getParcelableExtra(REPOSITORY_NAME)
        supportActionBar?.title = selectedRepository.name

        if (isNetworkStatusAvailable()) {
            repositoryDetailsViewModel = createViewModel(viewModelFactory) {
                setCurrentPage(1)
                setRepositoryName(selectedRepository.name)

                observe(stargazers) {
                    renderList(it)
                }

                observe(isRepositoryBookmarked()) {
                    fabBookmark.setFavouriteIcon(it)
                }
            }
        } else {
            swipeRefreshLayout.snack(getString(R.string.no_connection)) {
                initViewModel()
                it.dismiss()
            }
        }
    }

    private fun renderList(result: Resource<MutableList<StargazerModel>>?) {
        result?.let {
            when (it.status) {
                Status.LOADING -> swipeRefreshLayout.showLoading()
                Status.SUCCESS -> {
                    swipeRefreshLayout.hideLoading()

                    it.data?.let { data ->
                        if (data.isNotEmpty()) {
                            canLoadMore = true
                            if (!::adapter.isInitialized) {
                                adapter = StargazersListAdapter(data)
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
                    swipeRefreshLayout.snack(getString(R.string.error_message)) { snackbar ->
                        renderList(result)
                        snackbar.dismiss()
                    }
                }
            }
        }
    }

    private fun initViews() {
        fabBookmark = findViewById(R.id.fab_bookmark)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        recyclerView = findViewById(R.id.recycler_view)

        swipeRefreshLayout.isEnabled = false

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.apply {
            addOnScrollListener(object : InfiniteScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    if (canLoadMore) {
                        repositoryDetailsViewModel.setCurrentPage(page)
                    }
                }
            })
        }

        fabBookmark.setOnClickListener {
            repositoryDetailsViewModel.toggleBookmark()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REPOSITORY_NAME: String = "selected_repository"
        const val REPOSITORY_POSITION: String = "selected_position"
        const val REPOSITORY_POSITION_BOOKMARKED: String = "selected_position_bookmarked"
        const val REQUEST_CODE: Int = 545

        fun start(context: Context, repositoryModel: RepositoryModel, position: Int) {
            (context as AppCompatActivity).startActivityForResult(
                Intent(
                    context,
                    RepositoryDetailsActivity::class.java
                ).apply {
                    putExtra(REPOSITORY_NAME, repositoryModel)
                    putExtra(REPOSITORY_POSITION, position)
                }, REQUEST_CODE
            )
        }
    }
}