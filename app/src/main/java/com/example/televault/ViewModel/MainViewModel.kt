package com.example.televault.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.televault.Model.Series
import com.example.televault.Repository.MainRepository

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _searchQuery = MutableLiveData<String?>().apply { value = "" }

    val errorMessage = MutableLiveData<String>()

    fun getMovieList(): LiveData<PagingData<Series>> {
        return mainRepository.getAllSeries().cachedIn(viewModelScope)
    }
    val movies: LiveData<PagingData<Series>> = _searchQuery.switchMap { query ->
        Log.d("MainViewModel", "Search Query: $query")

        mainRepository.getAllSeries(query)
            .cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}