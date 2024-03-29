package com.soundapp.mobile.filmica.screens.utils

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.soundapp.mobile.filmica.R

// 0 Represents that we don't want to use the drag event
abstract class SwipeToDeleteCallback: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    override fun onMove(
            p0: RecyclerView,
            p1: RecyclerView.ViewHolder,
            p2: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView

        setColor(recyclerView, itemView, dX, c)

        setIcon(recyclerView, itemView, c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setIcon(
            recyclerView: RecyclerView,
            itemView: View,
            c: Canvas
    ) {
        val checkIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_check)!!

        val iconMargin = (itemView.height - checkIcon.intrinsicHeight) / 3
        val iconTop = itemView.top + (itemView.height - checkIcon.intrinsicHeight) / 2
        val iconLeft = itemView.left + iconMargin
        val iconRight = itemView.left + iconMargin + checkIcon.intrinsicWidth
        val iconBottom = iconTop + checkIcon.intrinsicHeight

        checkIcon.setBounds(
                iconLeft,
                iconTop,
                iconRight,
                iconBottom
        )

        checkIcon.draw(c)
    }

    private fun setColor(
            recyclerView: RecyclerView,
            itemView: View,
            dX: Float,
            c: Canvas
    ) {
        val color = ContextCompat.getColor(recyclerView.context, R.color.colorPrimaryDark)
        val background = ColorDrawable(color)
        background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
        )

        background.draw(c)
    }

}