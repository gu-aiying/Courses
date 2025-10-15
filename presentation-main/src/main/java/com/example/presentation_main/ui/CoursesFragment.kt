package com.example.presentation_main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.models.Course
import com.example.presentation_main.R
import com.example.presentation_main.databinding.FragmentCoursesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoursesViewModel by viewModels()

    private lateinit var adapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCoursesBinding.bind(view)

        setupAdapter()
        setupViews()
        observeViewModel()
    }

    private fun setupAdapter() {
        adapter = CoursesAdapter { course ->
            viewModel.toggleFavorite(course)
        }

        binding.coursesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CoursesFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupViews() {
        // Кнопка сортировки
        binding.sortButton.setOnClickListener {
            viewModel.toggleSorting()
            updateSortButtonText()
        }

        // Фильтр и поиск - нефункциональные по ТЗ
        binding.filterButton.isEnabled = false
        binding.filterButton.setOnClickListener {
            // Нефункциональный элемент
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { coursesState ->
                    handleState(coursesState)
                }
            }
        }
    }

    private fun handleState(state: CoursesState) {
        when (state) {
            is CoursesState.Loading -> showLoading()
            is CoursesState.Success -> showCourses(state.courses)
            is CoursesState.Error -> showError(state.message)
            is CoursesState.Empty -> showEmpty()
        }
        updateSortButtonText()
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.coursesRecyclerView.visibility = View.GONE
        binding.tvEmptyState.visibility = View.GONE
        binding.tvErrorState.visibility = View.GONE
    }

    private fun showCourses(courses: List<Course>) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.coursesRecyclerView.visibility = View.VISIBLE
        binding.tvEmptyState.visibility = View.GONE
        binding.tvErrorState.visibility = View.GONE

        adapter.updateList(courses)
    }

    private fun showError(message: String) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.coursesRecyclerView.visibility = View.GONE
        binding.tvEmptyState.visibility = View.GONE
        binding.tvErrorState.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.loadingProgressBar.visibility = View.GONE
        binding.coursesRecyclerView.visibility = View.GONE
        binding.tvEmptyState.visibility = View.VISIBLE
        binding.tvErrorState.visibility = View.GONE
    }

    private fun updateSortButtonText() {
        val currentState = viewModel.state.value
        val isSorted = currentState is CoursesState.Success && currentState.isSorted

        binding.sortButton.text = if (isSorted) {
            getString(R.string.sort_by_date_active)
        } else {
            getString(R.string.sort_by_date)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}