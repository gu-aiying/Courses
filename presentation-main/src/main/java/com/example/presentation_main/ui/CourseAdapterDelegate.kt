package com.example.presentation_main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Course
import com.example.presentation_main.R
import com.example.presentation_main.databinding.ItemCourseBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

class CourseAdapterDelegate(
    private val onFavoriteClick: (Course) -> Unit
) : AdapterDelegate<List<Course>>() {

    override fun isForViewType(items: List<Course>, position: Int): Boolean {
        return true // Только один тип элемента
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding, onFavoriteClick)
    }

    override fun onBindViewHolder(
        items: List<Course>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>
    ) {
        (holder as CourseViewHolder).bind(items[position])
    }

    class CourseViewHolder(
        private val binding: ItemCourseBinding,
        private val onFavoriteClick: (Course) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            with(binding) {
                courseTitle.text = course.title
                courseDescription.text = course.description
                coursePrice.text = course.price
                courseStartDate.text = course.startDate

                ratingView.text = course.rating.toString()

                if (course.image == null) {
                    ivCourse.setImageResource(com.example.design_system.R.drawable.course)
                } else {
                    // Загружать фото крусов из сервера
                }

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

}