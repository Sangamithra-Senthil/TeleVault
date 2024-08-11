package com.example.televault.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.televault.Model.Cast
import com.example.televault.databinding.ItemCastBinding

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private val castList = mutableListOf<Cast>()

    fun submitList(cast: List<Cast>) {
        castList.clear()
        castList.addAll(cast)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCastBinding.inflate(inflater, parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = castList[position]
        holder.bind(cast)
    }

    override fun getItemCount() = castList.size

    class CastViewHolder(private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.castName.text = cast.name
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w300${cast.profile_path}")
                .circleCrop()
                .into(binding.castImage)
        }
    }
}
