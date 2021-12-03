package com.challenge.searchapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.challenge.searchapp.R
import com.challenge.searchapp.ui.adapters.ItemsAdapter
import com.challenge.searchapp.ui.viewmodel.ItemsViewModel
import com.challenge.searchapp.util.Constants
import com.challenge.searchapp.util.Resource
import androidx.recyclerview.widget.DividerItemDecoration
import com.challenge.searchapp.databinding.FragmentSearchItemsBinding
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchItemsFragment : Fragment(R.layout.fragment_search_items) {

    lateinit var viewModel: ItemsViewModel
    lateinit var itemsAdapter: ItemsAdapter

    private var _binding: FragmentSearchItemsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchItemsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ItemsViewModel::class.java)
        //viewModel = (activity as ItemsActivity).viewModel
        setupRecyclerView()


        itemsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchItemsFragment_to_articleFragment,
                bundle
            )
        }

        if(viewModel.searchItemsResponse?.results.isNullOrEmpty()) {
            showEmptyResultMessage()
        } else {
            getQueryState()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                if(!binding.etSearch.text.isNullOrEmpty()) {
                    viewModel.searchItems(binding.etSearch.text.toString(), Constants.QUERY_PAGE_SIZE)
                    saveQueryState()
                }
            }
            false
        }

        viewModel.searchItems.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    if (response.data?.results?.count() != 0) {
                        hideEmptyResultMessage()
                    } else {
                        showEmptyResultMessage()
                    }
                    response.data?.let { itemsResponse ->
                        itemsAdapter.differ.submitList(itemsResponse.results.toList())

                        val totalPages = itemsResponse.results.count()
                        isLastPage = viewModel.limitItemsPage == totalPages || totalPages == itemsResponse.paging.total
                    }

                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    if(!isLastPage) {
                        showProgressBar()
                    }
                }
            }
        })
    }

    private fun saveQueryState() {
        val prefEditor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        prefEditor.putString("SEARCHQUERY", binding.etSearch.text.toString())
        prefEditor.apply()
    }

    private fun getQueryState() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val searchQuery = prefs.getString("SEARCHQUERY", "")
        binding.etSearch.setText(searchQuery)
    }

    private fun hideEmptyResultMessage() {
        binding.tvMessageResponse.visibility = View.INVISIBLE
        isEmptyPage = false
    }

    private fun showEmptyResultMessage() {
        binding.tvMessageResponse.visibility = View.VISIBLE
        isEmptyPage = true
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    var isEmptyPage = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.searchItems(binding.etSearch.text.toString(), totalItemCount + viewModel.searchItemsPage)
                isScrolling = false
            } else {
                binding.rvSearchItems.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        itemsAdapter = ItemsAdapter()
        binding.rvSearchItems.apply {
            adapter = itemsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchItemsFragment.scrollListener)
            val dividerItemDecoration = DividerItemDecoration(
                activity,
                LinearLayoutManager(activity).getOrientation()
            )
            binding.rvSearchItems.addItemDecoration(dividerItemDecoration)
        }
    }
}