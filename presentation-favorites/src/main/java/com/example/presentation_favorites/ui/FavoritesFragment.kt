package com.example.presentation_favorites.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Course
import com.example.presentation_favorites.R
import com.example.presentation_favorites.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var adapter: FavoritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        setupAdapter()
        observeViewModel()
    }

    private fun setupAdapter() {
        adapter = FavoritesAdapter { course ->
            viewModel.toggleFavorite(course)
        }

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoritesFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { favoriteState ->
                    handleState(favoriteState)
                }
            }
        }
    }

    private fun handleState(state: FavoritesState) {
        when (state) {
            is FavoritesState.Loading -> showLoading()
            is FavoritesState.Success -> showFavorites(state.courses)
            is FavoritesState.Empty -> showEmpty()
            is FavoritesState.Error -> showError(state.message)
        }
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.favoritesRecyclerView.visibility = View.GONE
        binding.tvEmptyState.visibility = View.GONE
    }

    private fun showFavorites(courses: List<Course>) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.favoritesRecyclerView.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE
        binding.tvErrorState.visibility = View.GONE

        adapter.submitList(courses)
    }

    private fun showEmpty() {
        binding.loadingProgressBar.visibility = View.GONE
        binding.favoritesRecyclerView.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.favoritesRecyclerView.visibility = View.GONE
        binding.tvEmptyState.visibility = View.GONE
        binding.tvErrorState.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}