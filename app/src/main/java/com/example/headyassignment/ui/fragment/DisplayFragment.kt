package com.example.headyassignment.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.headyassignment.R
import com.example.headyassignment.databinding.FragmentCategoryDisplayBinding
import com.example.headyassignment.ui.adapter.ItemAdapter
import com.example.headyassignment.utils.Utility
import com.example.headyassignment.utils.setGrid
import com.example.headyassignment.utils.setLinear
import android.view.MenuInflater
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.forEach


class DisplayFragment : BaseFragment() {

    private val TAG = "DisplayFragment"

    private lateinit var binding: FragmentCategoryDisplayBinding
    private lateinit var viewModel: DisplayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this)[DisplayViewModel::class.java]
        viewModel.withArgs(arguments)

        binding = FragmentCategoryDisplayBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewModel.showProduct) {
            inflater.inflate(R.menu.product_options, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sortBy) {
            showPopup(activity!!.findViewById(R.id.sortBy))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel?.showBackWithTitle(viewModel.categorySelectedName ?: "", viewModel.showGridView())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel.showGridView()) {
            val adapter = ItemAdapter(viewModel.categories, viewModel, false)
            binding.recyclerViewLayout.recyclerView.setGrid(adapter, 2)
        } else {
            val adapter = ItemAdapter(viewModel.categories, viewModel)
            binding.recyclerViewLayout.recyclerView.setLinear(adapter, false)
        }

        viewModel.getData()

        viewModel.notifyChange.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Utility.log(TAG, "notifyChange")
                binding.recyclerViewLayout.recyclerView.adapter?.notifyDataSetChanged()
                viewModel.notifyChange.value = null
            }
        })

        viewModel.itemClicked.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                if (it.isCategory()) {
                    navigate(
                        R.id.action_global_categoryDisplayFragment,
                        viewModel.getBundleForItem(it)
                    )
                }
                viewModel.itemClicked.value = null
            }
        })
    }


    private fun showPopup(v: View) {
        val wrapper = ContextThemeWrapper(v.context, R.style.MyPopupStyle)
        val popup = PopupMenu(wrapper, v)
        val inflater = popup.menuInflater
        popup.setOnMenuItemClickListener {
            viewModel.onSortOrderChanged(when (it.itemId) {
                R.id.byViews -> 0
                R.id.byOrder -> 1
                else -> 2
            })
            true
        }
        inflater.inflate(R.menu.product_sort_by_options, popup.menu)
        popup.menu.getItem(viewModel.productsSortOrder).isChecked = true
        popup.show()
    }

}