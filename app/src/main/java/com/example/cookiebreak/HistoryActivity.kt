package com.example.cookiebreak

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cookiebreak.MainActivity.Companion.TAG
import com.example.cookiebreak.adapter.ItemAdapter
import com.example.cookiebreak.data.Datasource
import com.example.cookiebreak.databinding.ActivityHistoryBinding
import com.example.cookiebreak.model.EatenCookiesModel
import com.example.cookiebreak.model.EatenCookies
import com.google.android.material.card.MaterialCardView

private lateinit var historyBinding: ActivityHistoryBinding


class HistoryActivity : AppCompatActivity() {

    private val viewModel: EatenCookiesModel by viewModels()
    //A tracker is what’s going to allow the selection library to track the selections of the user
    //var tracker: SelectionTracker<Long>? = null
    private var tracker: SelectionTracker<Long>? = null
    private lateinit var adapter: ItemAdapter
    private lateinit var myDataset: MutableList<EatenCookies>
    private lateinit var recyclerView: RecyclerView

    private fun itemClick(vh: ItemAdapter.ItemViewHolder){
        if(viewModel.selecting==false) {
//
//
//            Log.d(TAG, "ontracked ${vh.itemView.isClickable}")
//            vh.itemView.isClickable = false
//            vh.itemView.setSoundEffectsEnabled(false)
            return
        }
//        vh.itemView.isClickable = true
//        vh.itemView.setSoundEffectsEnabled(true)
        val pos  = vh.adapterPosition
        val card:MaterialCardView = vh.itemView as MaterialCardView
        //toggle entry from list
        if(viewModel.selectingList.contains(pos)) {
            viewModel.selectingList.remove(pos)
            Log.d(MainActivity.TAG, viewModel.selectingList.toString())
            card.setCardBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.selected_blue, null) )
        }else {
            Log.d(MainActivity.TAG, viewModel.selectingList.toString())
            viewModel.selectingList.add(pos);
            card.setCardBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.selected_red, null) )
        }
        adapter.notifyDataSetChanged()
        //01060012
        //17170450
        //ResourcesCompat.getColor(getResources(), R.color.selected_blue, null)
        //vh.itemView.background.setTint(Color.BLUE)

        //myDataset.removeAt(vh.adapterPosition)
        //adapter.notifyDataSetChanged()
        Log.d(MainActivity.TAG, "on recycel click ${pos}")
    }
    private fun itemLongClick(vh: ItemAdapter.ItemViewHolder){
        if(viewModel.selecting==false)
            return
        val pos  = vh.adapterPosition
        //myDataset.removeAt(vh.adapterPosition)
        adapter.notifyDataSetChanged()
        Log.d(MainActivity.TAG, "looooong")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            tracker?.onRestoreInstanceState(savedInstanceState);
        }
        historyBinding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        //list of data
        myDataset = Datasource().loadEatenCookies()
        recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
//        recyclerView.isSoundEffectsEnabled = false
//        val viewGroup = recyclerView
//        for (index in 0 until viewGroup.childCount) {
//            val nextChild = viewGroup.getChildAt(index)
//            Log.d(TAG, "ontracked ${nextChild}")
//            nextChild.isClickable = false
//            nextChild.setSoundEffectsEnabled(false)
//            //nextChild.
//        }
//        recyclerView.setOnTouchListener { view, motionEvent ->
//            when (motionEvent.action){
//            MotionEvent.ACTION_DOWN -> {
//                recyclerView.scrollBy(0,-20)
//            }
//            MotionEvent.ACTION_UP -> {
//                recyclerView.scrollBy(0,20)
//            }
//        }
//            return@setOnTouchListener true
//        }
        adapter = ItemAdapter(this, myDataset, ::itemClick, ::itemLongClick)
        recyclerView.adapter = adapter
        //only used to improve performance
        recyclerView.setHasFixedSize(true)

        //notify observers that data has changed
        adapter.notifyDataSetChanged()

        //setupTracker()
        historyBinding.selectButton.setOnClickListener {
            Log.d(TAG, "selectbutton")
            viewModel.selecting = !viewModel.selecting
            adapter.notifyDataSetChanged()
            setButtons()

        }

    }
    fun View.isUserInteractionEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup && this.childCount > 0) {
            this.children.forEach {
                it.isUserInteractionEnabled(enabled)
            }
        }
    }

    private fun ontouch(): Boolean {
        return true
    }
    private fun setButtons() {
        if(viewModel.selecting){
            historyBinding.deleteButton.setEnabled(true)
            historyBinding.deleteAllButton.setEnabled(false)
            //recyclerView.isClickable = true
            historyBinding.selectButton.setText(R.string.unselect)
        }else{
            historyBinding.deleteButton.setEnabled(false)
            historyBinding.deleteAllButton.setEnabled(true)
            //recyclerView.isClickable = false
            historyBinding.selectButton.setText(R.string.select)
            //unselect all items
           // tracker?.clearSelection()
        }

    }

    private fun setupTracker() {
        tracker = SelectionTracker.Builder<Long>(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val nItems: Int? = tracker?.selection!!.size()
                    if (nItems == 2) {
                        launchSum(tracker?.selection!!)
                    }
                }
            })

        adapter.tracker = tracker
    }
    private fun launchSum(selection: Selection<Long>) {
//        val list = selection.map {
//            adapter.list[it.toInt()]
//        }.toList()
//        SumActivity.launch(this, list as ArrayList<Int>)
        Log.d(TAG, "ontracked ${selection.toString()}")
    }
    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }
}