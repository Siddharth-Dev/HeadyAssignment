package com.example.headyassignment.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.headyassignment.data.model.Item
import com.example.headyassignment.data.source.db.entity.CategoryWithChildren
import com.example.headyassignment.data.source.db.entity.ProductWithVariants
import com.example.headyassignment.databinding.CategoryAdapterItemBinding
import com.example.headyassignment.databinding.ProductAdapterItemBinding
import com.example.headyassignment.databinding.SimpleAdapterItemBinding
import com.example.headyassignment.ui.BaseViewModel
import com.example.headyassignment.ui.adapter.ItemAdapter
import com.example.headyassignment.ui.fragment.DisplayViewModel
import com.example.headyassignment.utils.setFinalAspectRatio
import com.example.headyassignment.utils.setLinear

abstract class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun update(item: Item)
}

class CategoryViewHolder(val binding: CategoryAdapterItemBinding, val viewModel: BaseViewModel): BaseViewHolder(binding.root) {

    override fun update(item: Item) {
        binding.item = item
        binding.viewModel = viewModel
        if (item is CategoryWithChildren) {
            binding.recyclerView.setLinear(
                ItemAdapter(
                    item.subCategories,
                    viewModel,
                    false),
                true,
                .48f,
                true)
        }
    }
}

class SimpleViewHolder(val binding: SimpleAdapterItemBinding, val viewModel: BaseViewModel): BaseViewHolder(binding.root) {
    init {
        binding.root.setFinalAspectRatio(.42f, 16f/9)
    }
    override fun update(item: Item) {
        binding.item = item
        binding.viewModel = viewModel
    }
}

class ProductViewHolder(val binding: ProductAdapterItemBinding, val viewModel: BaseViewModel): BaseViewHolder(binding.root) {
    init {
        binding.root.setFinalAspectRatio(.42f, 3f/4, true)
    }
    override fun update(item: Item) {
        binding.item = item as ProductWithVariants
        binding.viewModel = viewModel as DisplayViewModel
    }
}