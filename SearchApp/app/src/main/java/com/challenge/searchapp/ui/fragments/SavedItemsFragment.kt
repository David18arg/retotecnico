package com.challenge.searchapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.challenge.searchapp.R
import com.challenge.searchapp.ui.adapters.ItemsAdapter
import com.challenge.searchapp.databinding.FragmentSavedItemsBinding
import com.challenge.searchapp.ui.viewmodel.ItemsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedItemsFragment : Fragment(R.layout.fragment_saved_items) {

    lateinit var viewModel: ItemsViewModel
    lateinit var itemsAdapter: ItemsAdapter
    private var _binding: FragmentSavedItemsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedItemsBinding.inflate(inflater, container, false)
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
                R.id.action_savedItemsFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = itemsAdapter.differ.currentList[position]
                viewModel.deleteArticle(item)
                Snackbar.make(view, "Articulo eliminado exitosamente", Snackbar.LENGTH_LONG).apply {
                    setAction("Deshacer eliminado") {
                        viewModel.saveArticle(item)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedItems)
        }

        viewModel.getSavedItems().observe(viewLifecycleOwner, Observer { articles ->
            hideEmptyResultMessage()
            if(articles.isEmpty()) {
                showEmptyResultMessage()
            }
            itemsAdapter.differ.submitList(articles)
        })
    }

    var isEmptyPage = false

    private fun hideEmptyResultMessage() {
        binding.tvSavedItemsResponse.visibility = View.INVISIBLE
        isEmptyPage = false
    }

    private fun showEmptyResultMessage() {
        binding.tvSavedItemsResponse.visibility = View.VISIBLE
        isEmptyPage = true
    }

    private fun setupRecyclerView() {
        itemsAdapter = ItemsAdapter()
        binding.rvSavedItems.apply {
            adapter = itemsAdapter
            layoutManager = LinearLayoutManager(activity)
            val dividerItemDecoration = DividerItemDecoration(
                activity,
                LinearLayoutManager(activity).getOrientation()
            )
            binding.rvSavedItems.addItemDecoration(dividerItemDecoration)
        }
    }
}