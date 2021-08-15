package com.markvangenderen.cookiebreak.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.markvangenderen.cookiebreak.R
import com.markvangenderen.cookiebreak.database.History
import com.markvangenderen.cookiebreak.databinding.ListItemBinding
import java.text.SimpleDateFormat
import java.util.*

//adapter for RecycleView with listAdapter
class ItemAdapter (private val onItemClicked: (ItemViewHolder, History) -> Unit,
                   private val setEffects: (ItemViewHolder) -> Unit)
    : ListAdapter<History, ItemAdapter.ItemViewHolder>(DiffCallback){

    class ItemViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        //set contents for item view
        fun bind(history: History) {
            binding.itemDate.text = convertLongToDateString(history.cookieTime)
            binding.itemPortion.text = when (history.cookiePortion) {
                0 -> binding.root.context.resources.getString(R.string.no_lower_case)
                1 -> "1/8"
                2 -> binding.root.context.resources.getString(R.string.quarter)
                3 -> "3/8"
                4 -> binding.root.context.resources.getString(R.string.half)
                5 -> "5/8"
                6 -> binding.root.context.resources.getString(R.string.three_quarter)
                7 -> "7/8"
                8 -> binding.root.context.resources.getString(R.string.full)
                else -> binding.root.context.resources.getString(R.string.eaten)
            } + binding.root.context.resources.getString(R.string.cookie_end)
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

        private fun convertLongToDateString(systemTime: Long): String {
            return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'hh:mm aa", Locale.getDefault())
                .format(systemTime).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout =  ListItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ))

        val vh = ItemViewHolder(adapterLayout)
        setEffects(vh)
        return vh
    }

    //used to replace the contents of a list item view
    //holder:ItemViewHolder previously created by the onCreateViewHolder()
    //position: the current item position
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(holder, current)
        }
        setEffects(holder)
    }


    //ItemAdapter callbacks
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

