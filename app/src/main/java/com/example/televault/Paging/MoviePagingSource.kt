package com.example.televault.Paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.televault.Model.Series
import com.example.televault.Service.RetrofitService
import java.security.Provider.Service


class MoviePagingSource(private val apiService: RetrofitService, private val query: String? ): PagingSource<Int, Series>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Series> {

        return try {
            val position = params.key ?: 1
            val response = if (query.isNullOrEmpty()) {
                apiService.getTopRatedMovies("d0736cc705ccd6f5e4e930cd770498c9", "en-US", position)
            } else {
                apiService.searchMovies("d0736cc705ccd6f5e4e930cd770498c9", query, position)
            }
            LoadResult.Page(
                data = response.body()?.results ?: emptyList(),
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.body()?.results.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Series>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}