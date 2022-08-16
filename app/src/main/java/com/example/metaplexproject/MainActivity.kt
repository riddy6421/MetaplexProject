package com.example.metaplexproject

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.metaplexproject.adapter.NftListAdapter
import com.example.metaplexproject.databinding.ActivityMainBinding
import com.example.metaplexproject.ui.UiEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchText: String
    private lateinit var nftListAdapter: NftListAdapter

    private val viewModel: NFTViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        onSearchButtonClick()

        lifecycleScope.launch {
            observeUiEvents()
        }
    }

    private fun searchNft(publicKey: String) {
        viewModel.searchPublicKey(publicKey)
    }

    private suspend fun observeUiEvents() = lifecycleScope.launch {
        viewModel.uiEvents.collectLatest { uiEvent ->
            when(uiEvent) {
                is UiEvents.Idle -> {
                    initDataRequest()
                }
                is UiEvents.Loading -> displayProgressBar()
                is UiEvents.Data -> {
                    hideProgressBar()
                    uiEvent.data.collectLatest {
                        nftListAdapter.submitData(it)
                    }
                    hideErrorTextView()
                }
                is UiEvents.Error -> {
                    hideProgressBar()
                    displayErrorTextView()
                }
            }
        }
    }

    private fun displayProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun displayErrorTextView() {
        binding.errorText.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun hideErrorTextView() {
        binding.errorText.visibility = View.GONE
    }

    private fun initDataRequest() {
        setSearchText()
        searchNft(searchText)
    }

    private fun setupRecyclerView() {
        nftListAdapter = NftListAdapter()
        binding.recyclerView.apply {
            adapter = nftListAdapter
            layoutManager = GridLayoutManager(applicationContext,2)
        }
    }

    private fun setSearchText() {
        searchText = if (binding.searchEdit.text.isEmpty()) {
            resources.getString(R.string.default_nft_public_key)
        } else {
            binding.searchEdit.text.toString()
        }
    }

    private fun onSearchButtonClick() {
        binding.searchBoxButton.setOnClickListener {
            if (binding.progressBar.visibility == View.VISIBLE) {
                return@setOnClickListener
            }
            initDataRequest()
        }
    }

}