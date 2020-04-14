package com.example.headyassignment.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.headyassignment.ui.adapter.ItemAdapter

fun RecyclerView.setLinear(adapter: ItemAdapter, isHorizontal: Boolean = true, widthRatio: Float = 1f, useWidthRatio: Boolean = false) {

    if (isHorizontal) {
        if (useWidthRatio) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        } else {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL))
    } else {
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }

    setAdapter(adapter)
}

fun RecyclerView.setGrid(adapter: ItemAdapter, spanCount: Int) {
    val layoutManager = GridLayoutManager(context, spanCount)
    addItemDecoration(DividerItemDecoration(context, RecyclerView.HORIZONTAL))
    addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))

    setLayoutManager(layoutManager)
    setAdapter(adapter)
}

fun View.setFinalAspectRatio(widthPer: Float?, ratio: Float, ignoreHeight: Boolean = false) {
    widthPer?.let {
        val width = resources.displayMetrics.widthPixels
        val itemWidth = (width * it).toInt()
        layoutParams.width = itemWidth
        layoutParams.height = if (ignoreHeight) ViewGroup.LayoutParams.WRAP_CONTENT else (itemWidth / ratio).toInt()
    }

}
