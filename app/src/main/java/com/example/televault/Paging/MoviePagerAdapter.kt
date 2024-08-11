package com.example.televault.Paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.televault.Model.Series
import com.example.televault.databinding.AdapterMovieBinding

class MoviePagerAdapter(private val onItemClicked: (Series) -> Unit) : PagingDataAdapter<Series, MoviePagerAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)!!
        movie?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterMovieBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }
    inner class MovieViewHolder(private val binding: AdapterMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(series: Series) {
            binding.name.text = series.name
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w300" + series.poster_path)
                .into(binding.imageview)

            binding.root.setOnClickListener {
                onItemClicked(series)

            }
        }

    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Series>() {
            override fun areItemsTheSame(oldItem: Series, newItem: Series): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Series, newItem: Series): Boolean {
                return oldItem == newItem
            }
        }
    }
}