package com.example.televault.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.televault.Model.Review
import com.example.televault.databinding.AdapterReviewBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews: List<Review> = listOf()

    fun setReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterReviewBinding.inflate(inflater, parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.binding.authorName.text = review.author
        var rating = review.author_details.rating?.toFloat() ?: 0f
        var ratingforfive =rating?.div(2)
        if (ratingforfive != null) {
            holder.binding.ratingBar.rating = ratingforfive
        }
        holder.binding.reviewText.text = review.content
        holder.binding.timeText.text = formatDate(review.created_at)
    }

    override fun getItemCount(): Int = reviews.size

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    class ReviewViewHolder(val binding: AdapterReviewBinding) : RecyclerView.ViewHolder(binding.root)
}
