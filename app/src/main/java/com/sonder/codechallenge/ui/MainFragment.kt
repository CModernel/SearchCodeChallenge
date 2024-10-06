package com.sonder.codechallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sonder.codechallenge.ui.models.SearchFragmentStates
import com.sonder.codechallenge.utils.repeatOnLifecycleWhenResumed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    private val viewModel: MainFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    MainScreenEntryPoint(viewModel)
                }
            }
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.repeatOnLifecycleWhenResumed {
            viewModel.state.collect { state ->
                when (state) {
                    is SearchFragmentStates.Started -> {
                        // Initial state
                    }
                    is SearchFragmentStates.ResultsLoaded -> {
                        // Update adapter with the results
                    }
                    is SearchFragmentStates.Error -> {
                        // Show error state
                    }
                }
            }
        }
    }
}