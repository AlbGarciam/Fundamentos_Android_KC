package com.soundapp.mobile.filmica.screens.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.R

class FilmsOffsetDecorator: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val offsetPX = view.context.resources.getDimensionPixelSize(R.dimen.offset_grid)
        val position = parent.getChildAdapterPosition(view) // From 0-9

        val items = parent.adapter?.itemCount ?: 0
        val columns = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1
        val rows = items / columns + 1

        val row = getRow(position, columns)
        val column = getColumn(position, columns)

        val topOffset = if (row == 1) offsetPX else offsetPX / 2
        val bottomOffset = if (row == rows) offsetPX else offsetPX / 2

        val leftOffset = if (column == 1) offsetPX else offsetPX / 2
        val rightOffset = if (column == columns) offsetPX else offsetPX / 2

        outRect.set(leftOffset, topOffset, rightOffset, bottomOffset)
    }

    private fun getRow(position: Int, columns: Int) = Math.ceil((position.toDouble() + 1) / columns.toDouble()).toInt()
    private fun getColumn(position: Int, columns: Int) = ( position % columns ) + 1
}