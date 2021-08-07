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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cookiebreak.MainActivity
import com.example.cookiebreak.CookieSelectFragment.Companion.TAG
import com.example.cookiebreak.R
import com.example.cookiebreak.database.History
import com.example.cookiebreak.databinding.ListItemBinding
import com.example.cookiebreak.model.EatenCookies
import com.example.cookiebreak.model.selectList
import com.example.cookiebreak.model.selected
import com.google.android.material.card.MaterialCardView

class ItemAdapter (private val itemLongClick: (ItemAdapter.ItemViewHolder) -> Unit,
                   private val onItemClicked: (History) -> Unit)
    : ListAdapter<History, ItemAdapter.ItemViewHolder>(DiffCallback){


    //ok, lets make model
    //create an ItemViewHolder class
    //retunrs a viewholder
    class ItemViewHolder(private var binding: ListItemBinding) :
        //??
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.itemDate.text = history.cookieTime.toString()
            binding.itemPortion.text = when (history.cookiePortion) {
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
            val drawableResource = when (history.cookiePortion) {
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
            binding.itemImage.setImageResource(drawableResource)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout =  ListItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ))

        val vh = ItemViewHolder(adapterLayout)
        setEffects(vh)
        Log.d(TAG, "onCreateViewHolder ${vh.adapterPosition}")
//        vh.itemView.setOnClickListener {
//            //itemClick(vh)
//        }
//        vh.itemView.setOnLongClickListener {
//            //itemLongClick(vh)
//            return@setOnLongClickListener true
//        }
        return vh
    }

    fun setEffects(vh: ItemAdapter.ItemViewHolder){
        if(selected.equals(false)){
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

        val card: MaterialCardView = holder.itemView as MaterialCardView
        if(selected.equals(true)){
            if(selectList.value?.contains(holder.adapterPosition) == true) {
                Log.d(TAG, selectList.toString())
                card.setCardBackgroundColor(Color.rgb(255,68,68))//red
            }else {
                Log.d(TAG, selectList.toString())
                card.setCardBackgroundColor(Color.rgb(51,181,229))//blue
            }
        }else{
            card.setCardBackgroundColor(Color.WHITE)
        }
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }

        Log.d(TAG, "onBindViewHolder ${holder.adapterPosition}")
        setEffects(holder)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return (oldItem.cookiePortion == newItem.cookiePortion) &&
                        (oldItem.cookieTime == newItem.cookieTime)
            }
        }
    }
}

