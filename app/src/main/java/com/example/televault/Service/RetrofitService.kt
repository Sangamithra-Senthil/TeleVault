package com.example.televault.Service

import com.example.televault.Model.CreditsResponse
import com.example.televault.Model.SeriesResponse
import com.example.televault.Model.ReviewResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {

    @GET("tv/popular")
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<SeriesResponse>

    @GET("tv/{series_id}/credits")
    suspend fun getTvShowCredits(
            @Path("series_id") series_id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<CreditsResponse>

    @GET("tv/{series_id}/similar")
    suspend fun getSimilarSeries(
        @Path("series_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Response<SeriesResponse>

    @GET("tv/{series_id}/reviews")
    suspend fun getTvShowReviews(
        @Path("series_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Response<ReviewResponse>

    @GET("search/tv")
    suspend fun searchMovies(
        @Query("api_key") api_Key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<SeriesResponse>
    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}