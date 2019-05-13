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

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        // Draw color
        setColor(recyclerView, itemView, dX, c)

        setIcon(recyclerView, itemView, c)
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setIcon(recyclerView: RecyclerView, itemView: View, c: Canvas) {
        // Draw the icon
        val checkIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_check)!!
        // Vertical centering
        val iconMargin = (itemView.height - checkIcon.intrinsicHeight) / 3
        val iconTop = itemView.top + (itemView.height - checkIcon.intrinsicHeight) / 2
        val iconLeft = itemView.left + iconMargin
        val iconRight = itemView.left + iconMargin + checkIcon.intrinsicWidth
        val iconBottom = iconTop + checkIcon.intrinsicHeight
        checkIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        checkIcon.draw(c)
    }

    private fun setColor(recyclerView: RecyclerView, itemView: View, dX: Float, c: Canvas) {
        var color = ContextCompat.getColor(recyclerView.context, R.color.colorPrimaryDark)
        val background = ColorDrawable(color)

        background.setBounds(itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom)
        background.draw(c)
    }

}