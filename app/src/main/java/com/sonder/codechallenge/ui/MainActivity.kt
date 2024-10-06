package com.sonder.codechallenge.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sonder.codechallenge.R
import com.sonder.codechallenge.databinding.ActivityMainBinding
import com.sonder.codechallenge.ui.models.SearchActivityStates
import com.sonder.codechallenge.ui.theme.SonderCodeChallengeTheme
import com.sonder.codechallenge.utils.repeatOnLifecycleWhenResumed
import com.sonder.common.safeString
import com.sonder.domain.models.SearchSectionResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindViewActions()
        observeViewModel()
    }

    private fun bindViewActions() {
        setSupportActionBar(binding.searchToolbar)
        binding.apply {
            etSearchToolbar.setOnQueryTextListener(createQueryTextListener())
        }
    }

    private fun observeViewModel() {
        repeatOnLifecycleWhenResumed {
            viewModel.state.collect { state ->
                when (state) {
                    is SearchActivityStates.Started -> {
                        binding.searchProgressBar.visibility = View.GONE
                        binding.tvSearchTitle.text = getString(R.string.search_started_text)
                        clearFragments()
                    }
                    is SearchActivityStates.Searching -> {
                        binding.tvSearchTitle.text = getString(R.string.searching_for_text, state.query)
                        binding.searchProgressBar.visibility = View.VISIBLE
                        addMainFragment()
                    }
                    is SearchActivityStates.Results -> {
                        binding.tvSearchTitle.text = getString(R.string.showing_results_for_text, state.query)
                        binding.searchProgressBar.visibility = View.GONE
                    }
                    is SearchActivityStates.Error -> {
                        binding.tvSearchTitle.text = state.message
                        binding.searchProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun clearFragments() {
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().remove(it).commitNowAllowingStateLoss()
        }
    }

    private fun addMainFragment() {
        val fragmentTag = MainFragment::class.java.simpleName
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
            ?: MainFragment.newInstance()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.searchFragmentContainer, fragment, fragmentTag)
            commitNow()
        }
    }

    private fun createQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query.safeString().length > 2) {
                binding.etSearchToolbar.clearFocus()
                viewModel.updateSearchQuery(query.safeString())
            } else {
                viewModel.clearSearchQuery()
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText.isNullOrEmpty()) {
                viewModel.clearSearchQuery()
            }
            return false
        }
    }
}