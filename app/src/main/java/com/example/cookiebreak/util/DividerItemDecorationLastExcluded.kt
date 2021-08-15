package com.example.cookiebreak.util

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Canvas
import androidx.core.view.get

class DividerItemDecorationLastExcluded(val context: Context, private val dividerDrawable: Drawable) :
    RecyclerView.ItemDecoration() {

    var width:Int = 0
    var height:Int = 0

    init {
        desiredDimensions()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        //give the offset of the height of the drawable to eache item
        // except the last(where nothing is drawn)(last element is in position zero since
        // RecycleView is set to reverse order)
        if(parent.getChildAdapterPosition(view) != 0)
            outRect.set(0,0,0,height)

    }

    //determine suitable width and height values for the bounding rectangle which produces a width
    //of 20dp without distorting(stretching) the drawing
    fun desiredDimensions(){
        height = context.resources.displayMetrics.run { density*15 }.toInt()
        width = dividerDrawable.intrinsicWidth/dividerDrawable.intrinsicHeight*height
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        var left:Int
        var right:Int
        var top:Int
        var bottom:Int
        //set as much as you can of the drawable, then cut it when its about to stretch
        var count = parent.childCount -1
        var v:View
        //don't include the last child(only want decoration between items)
        while (count>0){
            v = parent.get(count)
            //return left corner position of view's rectangle
            left = v.left  //place aligned with left side of views
            top = v.bottom //place bellow the view
            right = left + width
            bottom = top + height
            dividerDrawable.setBounds(left,top,right,bottom)
            dividerDrawable.draw(c)
            count--
        }

    }
}