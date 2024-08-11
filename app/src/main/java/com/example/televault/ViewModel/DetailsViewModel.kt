package com.example.televault.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.televault.Model.Cast
import com.example.televault.Model.Review
import com.example.televault.Model.Series
import com.example.televault.Repository.MainRepository
import kotlinx.coroutines.launch

class DetailsViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _cast = MutableLiveData<List<Cast>>()
    val cast: LiveData<List<Cast>> get() = _cast
    private val _similarSeries = MutableLiveData<List<Series>>()
    val similarSeries: LiveData<List<Series>> get() = _similarSeries

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    //Method to load the cast
    fun loadTvShowCredits(tvId: Int) {
        viewModelScope.launch {
            val creditsResponse = mainRepository.getTvShowCredits(tvId)
            _cast.postValue(creditsResponse?.cast ?: emptyList())
        }
    }
    //Method to load similar series
    fun loadSimilarSeries(tvId: Int) {
        viewModelScope.launch {
            val response = mainRepository.getSimilarSeries(tvId)
            _similarSeries.postValue(response?.results ?: emptyList())
        }
    }
    //Method to load reviews
    fun loadTvShowReviews(tvId: Int) {
        viewModelScope.launch {
            val response = mainRepository.getTvShowReviews(tvId)
            _reviews.postValue(response?.results ?: emptyList())
        }
    }
}
