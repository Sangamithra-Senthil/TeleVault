package com.example.televault.View.Details

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.televault.adapter.CastAdapter
import com.example.televault.Model.Series
import com.example.televault.R
import com.example.televault.Repository.MainRepository
import com.example.televault.adapter.ReviewAdapter
import com.example.televault.Service.RetrofitService
import com.example.televault.adapter.SimilarSeriesAdapter
import com.example.televault.ViewModel.DetailsViewModel
import com.example.televault.ViewModel.DetailsViewModelFactory

class DetailsActivity : AppCompatActivity() {
    lateinit var castRecylerView:RecyclerView
    lateinit var viewModel: DetailsViewModel
    lateinit var similarSeriesRecyclerView:RecyclerView
    lateinit var reviewRecyclerView: RecyclerView
    private val similarSeriesAdapter = SimilarSeriesAdapter { series ->
        // Handle click event for similar series
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("MOVIE_EXTRA", series)
        intent.putExtra("TV_ID", series.id)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        castRecylerView = findViewById(R.id.cast_recycler_view)
        similarSeriesRecyclerView =findViewById(R.id.similar_series_recycler_view)
        reviewRecyclerView = findViewById(R.id.reviews_recycler_view)
        val movie = intent.getParcelableExtra<Series>("MOVIE_EXTRA")
        findViewById<TextView>(R.id.title).text = movie?.name

        val backdrop = findViewById<ImageView>(R.id.movie_backdrop)
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500" + movie?.backdrop_path)
            .into(backdrop)
        val poster = findViewById<ImageView>(R.id.movie_poster)
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500" + movie?.poster_path)
            .into(poster)
        val voteAverage10 = movie?.vote_average // this is the vote average out of 10
        val voteAverage5 = voteAverage10?.div(2) // this will convert it to a 5-point scale
        findViewById<TextView>(R.id.movie_overview).text = movie?.overview
        findViewById<TextView>(R.id.movie_release_date).text = movie?.first_air_date
        if (voteAverage5 != null) {
            findViewById<RatingBar>(R.id.movie_rating).rating = voteAverage5
        }
        val tvId = intent.getIntExtra("TV_ID", 0)
        val mainRepository = MainRepository(RetrofitService.getInstance())
        viewModel = ViewModelProvider(this, DetailsViewModelFactory(mainRepository)).get(DetailsViewModel::class.java)

        viewModel.loadTvShowCredits(tvId)

        viewModel.cast.observe(this) { cast ->
            val castAdapter = CastAdapter()
            castRecylerView.adapter = castAdapter
            castRecylerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            castAdapter.submitList(cast)
        }
        similarSeriesRecyclerView.adapter = similarSeriesAdapter
        similarSeriesRecyclerView.layoutManager = GridLayoutManager(this,2)
        viewModel.loadSimilarSeries(tvId)

        viewModel.similarSeries.observe(this) { series ->
            similarSeriesAdapter.submitList(series)
        }
        val reviewAdapter = ReviewAdapter()
        reviewRecyclerView.adapter = reviewAdapter
        reviewRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        viewModel.loadTvShowReviews(tvId)

        viewModel.reviews.observe(this) { reviews ->
            reviewAdapter.setReviews(reviews)
        }
    }
}