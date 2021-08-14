package com.example.cookiebreak.util

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Canvas
import androidx.core.view.children

class DividerItemDecorationLastExcluded(private val dividerDrawable: Drawable) :
    RecyclerView.ItemDecoration() {

    private val dividerWidth = dividerDrawable.intrinsicWidth
    private val dividerHeight = dividerDrawable.intrinsicHeight

    //the spacing for the thing we-re try to add
    override fun getItemOffsets(rect: Rect, v: View, parent: RecyclerView, s: RecyclerView.State) {
        parent.adapter?.let { adapter ->
            val childAdapterPosition = parent.getChildAdapterPosition(v)
                .let { if (it == RecyclerView.NO_POSITION) return else it }
            rect.top = // Add space/"padding" on right side
                if (childAdapterPosition == adapter.itemCount - 1) 0    // No "padding"
                else dividerHeight                                       // Drawable width "padding"
        }
    }

    //the thing we're adding
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.adapter?.let { adapter ->
            parent.children // Displayed children on screen
                .forEach { view ->
                    val childAdapterPosition = parent.getChildAdapterPosition(view)
                        .let { if (it == RecyclerView.NO_POSITION) return else it }
                    if (childAdapterPosition != adapter.itemCount ) {
                        val top = view.bottom
                        val left = parent.paddingLeft
                        val bottom = top + dividerHeight
                        val right = view.right
                        dividerDrawable.bounds = Rect(left, top, right, bottom)
                        dividerDrawable.draw(canvas)
                    }
                }
        }
    }
}