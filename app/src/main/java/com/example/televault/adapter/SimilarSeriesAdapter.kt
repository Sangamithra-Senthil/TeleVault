package com.example.televault.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.televault.Model.Series
import com.example.televault.databinding.AdapterMovieBinding

class SimilarSeriesAdapter(private val onItemClicked: (Series) -> Unit) :
    RecyclerView.Adapter<SimilarSeriesAdapter.SimilarSeriesViewHolder>() {

    private val similarSeries = mutableListOf<Series>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarSeriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterMovieBinding.inflate(inflater, parent, false)
        return SimilarSeriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimilarSeriesViewHolder, position: Int) {
        val movie = similarSeries[position]
        holder.binding.name.text = movie.name
        Glide.with(holder.itemView.context).load("https://image.tmdb.org/t/p/w300" + movie.poster_path).into(holder.binding.imageview)

        holder.itemView.setOnClickListener {
            onItemClicked(movie)
        }
    }

    override fun getItemCount(): Int = similarSeries.size

    fun submitList(list: List<Series>) {
        similarSeries.clear()
        similarSeries.addAll(list)
        notifyDataSetChanged()
    }

    class SimilarSeriesViewHolder(val binding: AdapterMovieBinding) : RecyclerView.ViewHolder(binding.root)
}
