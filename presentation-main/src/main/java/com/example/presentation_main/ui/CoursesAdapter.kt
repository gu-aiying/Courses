package com.example.presentation_main.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.Course
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CoursesAdapter(
    onFavoriteClick: (Course) -> Unit
) : AsyncListDifferDelegationAdapter<Course>(CourseDiffCallback) {

    init {
        delegatesManager.addDelegate(CourseAdapterDelegate(onFavoriteClick))
    }

    fun updateList(newList: List<Course>) {
        items = newList
    }
}

object CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Course, newItem: Course): Any? {
        return if (oldItem.isFavorite != newItem.isFavorite) {
            mapOf("isFavorite" to newItem.isFavorite)
        } else {
            null
        }
    }
}