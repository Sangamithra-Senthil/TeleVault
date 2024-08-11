package com.example.televault.View.HomePage

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.televault.Paging.MoviePagerAdapter
import com.example.televault.R
import com.example.televault.Repository.MainRepository
import com.example.televault.Service.RetrofitService
import com.example.televault.View.Details.DetailsActivity
import com.example.televault.ViewModel.MainViewModel
import com.example.televault.ViewModel.MyViewModelFactory
import com.example.televault.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        val adapter = MoviePagerAdapter { selectedMovie ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("MOVIE_EXTRA", selectedMovie)
            intent.putExtra("TV_ID", selectedMovie.id)
            startActivity(intent)
        }

        binding.recyclerview.adapter = adapter
        swipeRefreshLayout = findViewById(R.id.Swipe)
        searchView = findViewById(R.id.search_view)

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        adapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading)
                binding.progressDialog.isVisible = true
            else {
                // Stop the refreshing animation
                swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
                binding.progressDialog.isVisible = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                    binding.progressDialog.isVisible = false
                    swipeRefreshLayout.isRefreshing = false

                }

            }
        }

        lifecycleScope.launch {
            adapter.submitData(PagingData.empty())

            viewModel.movies.observe(this@MainActivity) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {

                adapter.refresh()
                swipeRefreshLayout.isRefreshing = false

            }
        }
        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {

                    viewModel.setSearchQuery(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.setSearchQuery(it)
                }
                return true
            }
        })
    }

}