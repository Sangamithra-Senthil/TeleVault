package com.example.televault.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


const val NETWORK_PAGE_SIZE = 25

data class SeriesResponse(val page: Int, val results: List<Series>)

@Parcelize
data class Series(
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val first_air_date: String?,
    val vote_average: Float?,
    val backdrop_path: String?,
) : Parcelable

data class CreditsResponse(
    val cast: List<Cast>
)

data class Cast(
    val id: Int,
    val name: String,
    val profile_path: String?
)

data class ReviewResponse(
    val results: List<Review>
)

data class Review(
    val author: String,
    val content: String,
    val created_at: String,
    val author_details: AuthorDetails
)

data class AuthorDetails(
    val rating: Float?
)