package com.example.cookiebreak.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cookiebreak.CookieSelectFragment.Companion.TAG
import com.example.cookiebreak.R
import com.example.cookiebreak.database.History
import com.example.cookiebreak.databinding.ListItemBinding
import java.text.SimpleDateFormat

class ItemAdapter (private val itemLongClick: (ItemAdapter.ItemViewHolder) -> Unit,
                   private val onItemClicked: (ItemAdapter.ItemViewHolder, History) -> Unit,
                   private val setEffects: (ItemAdapter.ItemViewHolder) -> Unit)
    : ListAdapter<History, ItemAdapter.ItemViewHolder>(DiffCallback){


    //ok, lets make model
    //create an ItemViewHolder class
    //retunrs a viewholder
    class ItemViewHolder(private var binding: ListItemBinding) :
        //??
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.itemDate.text = convertLongToDateString(history.cookieTime)
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

        fun convertLongToDateString(systemTime: Long): String {
            return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
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

    //used to replace the contents of a list item view
    //holder:ItemViewHolder previously created by the onCreateViewHolder()
    //position: the current item position
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener {
            onItemClicked(holder, current)
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

