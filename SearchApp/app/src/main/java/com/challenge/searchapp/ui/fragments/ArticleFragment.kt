package com.challenge.searchapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.challenge.searchapp.R
import com.challenge.searchapp.ui.viewmodel.ItemsViewModel
import com.challenge.searchapp.util.Resource
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.NumberFormat
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import com.challenge.searchapp.databinding.FragmentArticleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: ItemsViewModel

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ItemsViewModel::class.java)
        //viewModel = (activity as ItemsActivity).viewModel

        var article = args.article
        viewModel.findArticle(article?.id.toString())

        viewModel.findArticle.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { itemsResponse ->
                        binding.productName.text = itemsResponse.title
                        binding.productImage.apply {
                            Glide.with(this)
                                .asBitmap()
                                .load(itemsResponse.pictures?.get(0)?.secure_url)
                                .into(binding.productImage)
                        }
                        val formatter: NumberFormat = DecimalFormat(getString(R.string.format_decimal))

                        binding.productPrice.text = getString(R.string.label_price) + formatter.format(itemsResponse.price?.toDouble())
                        if (itemsResponse.condition.equals(getString(R.string.value_new))) {
                            binding.productStatus.text = getString(R.string.label_new)
                        } else {
                            binding.productStatus.text = getString(R.string.label_used)
                        }
                        if (itemsResponse.accepts_mercadopago.equals(getString(R.string.value_true))) {
                            binding.productAcceptMP.text = getString(R.string.label_mercado_pago)
                        }
                        binding.productUnit.text = getString(R.string.label_unit) + itemsResponse.available_quantity
                        binding.productSoldQuantity.text = itemsResponse.sold_quantity + getString(R.string.label_sold_quantity)

                        if(itemsResponse.attributes?.count() != 0) {
                            var initColor = true
                            itemsResponse.attributes?.map { item ->
                                val tbrow = TableRow(activity)
                                val tvLeftColumn = TextView(activity)
                                tvLeftColumn.text = item.name
                                tvLeftColumn.setTextColor(Color.BLACK)
                                tvLeftColumn.setPadding(25)
                                val params: TableRow.LayoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                                tvLeftColumn.setLayoutParams(params)
                                tbrow.addView(tvLeftColumn)

                                val tvRightColumn = TextView(activity)
                                tvRightColumn.text = item.valueName
                                tvRightColumn.setTextColor(Color.BLACK)
                                tvLeftColumn.setPadding(25)
                                tvRightColumn.setLayoutParams(params)
                                tbrow.addView(tvRightColumn)

                                if (initColor) {
                                    tbrow.setBackgroundColor(Color.LTGRAY)
                                    initColor = false
                                } else {
                                    tbrow.setBackgroundColor(Color.WHITE)
                                    initColor = true
                                }
                                tbrow.top = 15

                                binding.tlDescriptionTable.addView(tbrow)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(view, getString(R.string.response_message_error), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, getString(R.string.saved_message_success), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
}
