package com.example.headyassignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headyassignment.data.model.Item
import com.example.headyassignment.databinding.CategoryAdapterItemBinding
import com.example.headyassignment.databinding.ProductAdapterItemBinding
import com.example.headyassignment.databinding.SimpleAdapterItemBinding
import com.example.headyassignment.ui.BaseViewModel
import com.example.headyassignment.ui.adapter.viewholder.BaseViewHolder
import com.example.headyassignment.ui.adapter.viewholder.CategoryViewHolder
import com.example.headyassignment.ui.adapter.viewholder.ProductViewHolder
import com.example.headyassignment.ui.adapter.viewholder.SimpleViewHolder

class ItemAdapter(val items: List<Item>, val viewModel: BaseViewModel, val showCategory: Boolean = true) : RecyclerView.Adapter<BaseViewHolder>() {

    private val TYPE_CATEGORY = 0
    private val TYPE_PRODUCT = 1
    private val TYPE_DEFAULT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> CategoryViewHolder(CategoryAdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), viewModel)
            TYPE_PRODUCT -> ProductViewHolder(ProductAdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), viewModel)
            else -> SimpleViewHolder(SimpleAdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), viewModel)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when {
            item.isCategory() && showCategory -> TYPE_CATEGORY
            !item.isCategory() -> TYPE_PRODUCT
            else -> TYPE_DEFAULT
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.update(items[position])
    }
}