package com.challenge.searchapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.challenge.searchapp.databinding.ItemArticlePreviewBinding
import com.challenge.searchapp.model.ItemDetails
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat


class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<ItemDetails>() {
        override fun areItemsTheSame(oldItem: ItemDetails, newItem: ItemDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemDetails, newItem: ItemDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((ItemDetails) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            holder.binding.tvTitle.text = article?.title
            Glide.with(this)
                .asBitmap()
                .load(article.thumbnail)
                .fitCenter()
                .dontTransform()
                .into(holder.binding.ivArticleImage)
            val formatter: NumberFormat = DecimalFormat("#,###.00")
            holder.binding.tvProdPrice.text = "$ "+ formatter.format(article.price?.toDouble())
            holder.binding.tvProdUnit.text = "Stock disponible "+article.available_quantity
            if (article.accepts_mercadopago.equals("true")) {
                holder.binding.tvProdAcceptMP.text = "Mercado Pago"
            }
            if (article.condition.equals("new")) {
                holder.binding.prodStatus.text = "Nuevo  |"
            } else {
                holder.binding.prodStatus.text = "Usado  |"
            }
            holder.binding.prodSoldQuantity.text = article.sold_quantity+" Vendido"

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnItemClickListener(listener: (ItemDetails) -> Unit) {
        onItemClickListener = listener
    }
}













