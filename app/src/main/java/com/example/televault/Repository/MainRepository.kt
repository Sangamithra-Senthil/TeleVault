package com.example.televault.Repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.televault.Model.CreditsResponse
import com.example.televault.Model.SeriesResponse
import com.example.televault.Model.NETWORK_PAGE_SIZE
import com.example.televault.Model.ReviewResponse
import com.example.televault.Model.Series
import com.example.televault.Paging.MoviePagingSource
import com.example.televault.Service.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository constructor(private val retrofitService: RetrofitService) {

    // Fetch series details
    fun getAllSeries(query: String? = null): LiveData<PagingData<Series>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2
            ),
            pagingSourceFactory = {
                MoviePagingSource(retrofitService, query)
            }
            , initialKey = 1
        ).liveData
    }
    //Fetch cast details
    suspend fun getTvShowCredits(tvId: Int): CreditsResponse? {
        val YOUR_API_KEY = "d0736cc705ccd6f5e4e930cd770498c9"
        val response = retrofitService.getTvShowCredits(tvId, YOUR_API_KEY)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    //Fetch similar series details
    suspend fun getSimilarSeries(tvId: Int): SeriesResponse? {
        val YOUR_API_KEY = "d0736cc705ccd6f5e4e930cd770498c9"
        val response = retrofitService.getSimilarSeries(tvId, YOUR_API_KEY, "en-US")
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    //Fetch reviews
    suspend fun getTvShowReviews(tvId: Int): ReviewResponse? {
        val YOUR_API_KEY = "d0736cc705ccd6f5e4e930cd770498c9"

        return withContext(Dispatchers.IO) {
            try {
                val response = retrofitService.getTvShowReviews(tvId, YOUR_API_KEY)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}