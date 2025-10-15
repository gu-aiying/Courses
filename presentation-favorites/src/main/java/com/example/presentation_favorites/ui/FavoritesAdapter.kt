package com.example.presentation_favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Course
import com.example.presentation_favorites.R
import com.example.presentation_favorites.databinding.ItemCourseBinding

class FavoritesAdapter(
    private val onFavoriteClick: (Course) -> Unit
) : ListAdapter<Course, FavoritesAdapter.FavoriteViewHolder>(FavoriteDiffCallback) {

    inner class FavoriteViewHolder(
        private val binding: ItemCourseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            with(binding) {
                courseTitle.text = course.title
                courseDescription.text = course.description
                coursePrice.text = course.price
                courseStartDate.text = course.startDate

                ratingView.text = course.rating.toString()

                val iconRes = if (course.isFavorite) {
                    R.drawable.ic_favorite_true
                } else {
                    R.drawable.ic_favorite
                }
                favoriteButton.setImageResource(iconRes)

                favoriteButton.setOnClickListener {
                    onFavoriteClick(course)
                }

                root.setOnClickListener {
                    // Навигация к деталям курса
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object FavoriteDiffCallback : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }
}