package com.example.cookiebreak

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookiebreak.adapter.ItemAdapter

//Given that motion event we will find in which child of the RecyclerView the event happened and we will return the details of that item.
class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as ItemAdapter.ItemViewHolder)
                .getItemDetails()
        }
        return null
    }
}