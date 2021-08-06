package com.example.cookiebreak.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.cookiebreak.MainActivity
import com.example.cookiebreak.MainActivity.Companion.TAG
import com.example.cookiebreak.R
import com.example.cookiebreak.model.EatenCookies
import com.example.cookiebreak.model.selectList
import com.example.cookiebreak.model.selected
import com.google.android.material.card.MaterialCardView

class ItemAdapter (private val context: Context, private val dataset: MutableList<EatenCookies>,
                   private val itemClick: (ItemAdapter.ItemViewHolder) -> Unit,
                   private val itemLongClick: (ItemAdapter.ItemViewHolder) -> Unit)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){
    init {
        //tell the RecyclerView that each item in the data set can be represented with a unique identifier of type Long
        setHasStableIds(true)
    }
    var tracker: SelectionTracker<Long>? = null

    //ok, lets make model
    //create an ItemViewHolder class
    //retunrs a viewholder
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textViewPortion: TextView = view.findViewById(R.id.item_portion)
        val textViewDate: TextView = view.findViewById(R.id.item_date)
        val imageView: ImageView = view.findViewById(R.id.item_image)

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
        fun bind(value: EatenCookies, isActivated: Boolean = false) {
            //done elsewhere
            //text.text = value.toString()
            //whats itemView?
            itemView.isActivated = isActivated
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //inflate item XML layout resource
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        //pass in a reference to the list item view as argument

        val vh = ItemViewHolder(adapterLayout)
        setEffects(vh)
        Log.d(TAG, "onCreateViewHolder ${vh.adapterPosition}")
        vh.itemView.setOnClickListener {
            itemClick(vh)
        }
        vh.itemView.setOnLongClickListener {
            itemLongClick(vh)
            return@setOnLongClickListener true
        }
        return vh
    }

    fun setEffects(vh: ItemAdapter.ItemViewHolder){
        if(selected==false){
            //not clickable, and no sound effects
            vh.itemView.isClickable = false
            vh.itemView.isSoundEffectsEnabled = false
        }
        else{
            vh.itemView.isClickable = true
            vh.itemView.isSoundEffectsEnabled = true
        }
    }

    //used to replace the contents of a list item view
    //holder:ItemViewHolder previously created by the onCreateViewHolder()
    //position: the current item position
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = dataset[position]
        //you can call getString() with a string resource ID, and it will return the string value associated with it.
        val size = context.resources.getString(item.portionResourceId).toInt()
        val date = context.resources.getString(item.dateResourceId)
        holder.textViewDate.text = date;
        val card: MaterialCardView = holder.itemView as MaterialCardView
        if(selected){
            if(selectList.contains(holder.adapterPosition)) {
                Log.d(MainActivity.TAG, selectList.toString())
                card.setCardBackgroundColor(Color.rgb(255,68,68))//red
            }else {
                Log.d(MainActivity.TAG, selectList.toString())
                card.setCardBackgroundColor(Color.rgb(51,181,229))//blue
            }
        }else{
            card.setCardBackgroundColor(Color.WHITE)
        }

        eatenCookieShow(holder, size)
        Log.d(TAG, "onBindViewHolder ${holder.adapterPosition}")
        setEffects(holder)
        //tell the ViewHolder if the item in that position has been selected by the user or not
        tracker?.let {
            holder.bind(item, it.isSelected(position.toLong()))
        }
    }

    override fun getItemCount() = dataset.size

    //return the position of the item as its id
    //each element's position in the list is its id for our purposes
    override fun getItemId(position: Int): Long = position.toLong()

    private fun eatenCookieShow(holder: ItemViewHolder, size: Int) {
        holder.textViewPortion.text = when (size) {
            0 -> "no"
            1 -> "1/8"
            2 -> "quarter"
            3 -> "3/8"
            4 -> "half"
            5 -> "5/8"
            6 -> "three quarter"
            7 -> "7/8"
            else -> "full"
        } + " cookie"
        val drawableResource = when (size) {
            0 -> R.drawable.ic_cookie_none
            1 -> R.drawable.ic_cookie_1
            2 -> R.drawable.ic_cookie_2
            3 -> R.drawable.ic_cookie_3
            4 -> R.drawable.ic_cookie_4
            5 -> R.drawable.ic_cookie_5
            6 -> R.drawable.ic_cookie_6
            7 -> R.drawable.ic_cookie_7
            else -> R.drawable.ic_cookie_full
        }

        holder.imageView.setImageResource(drawableResource)
    }

    private fun setOnLongClickListener(): Boolean {
        Log.d(MainActivity.TAG, "loooong")
        return true
    }
}

