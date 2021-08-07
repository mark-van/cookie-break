package com.example.cookiebreak

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.cookiebreak.adapter.ItemAdapter
import com.example.cookiebreak.database.CookieBreakApplication
import com.example.cookiebreak.database.History
import com.example.cookiebreak.databinding.FragmentCookieHistoryBinding
import com.example.cookiebreak.model.EatenCookiesModel
import com.example.cookiebreak.model.EatenCookiesModelFactory
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder


//lateinit var history: History

class CookieHistoryFragment : Fragment() {

    private var _binding: FragmentCookieHistoryBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val TAG = "Main"
    }
    //activityViewModels() gives you the ViewModel instance scoped to the current activity.
    // //Therefore the instance will remain the same across multiple fragments in the same activity.
    private val historyViewModel: EatenCookiesModel by activityViewModels{
        EatenCookiesModelFactory(
            (activity?.application as CookieBreakApplication).database.scheduleDao()
        )//diff?
    }
    private lateinit var adapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView

    private fun itemClick(vh: ItemAdapter.ItemViewHolder){
        if(historyViewModel.selecting==false) {
            return
        }
        val pos  = vh.adapterPosition
        val card: MaterialCardView = vh.itemView as MaterialCardView
        //toggle entry from list
        if(historyViewModel.selectingList.contains(pos)) {
            historyViewModel.selectingList.remove(pos)
            Log.d(TAG, historyViewModel.selectingList.toString())
            card.setCardBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.selected_blue, null) )
        }else {
            Log.d(TAG, historyViewModel.selectingList.toString())
            historyViewModel.selectingList.add(pos);
            card.setCardBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.selected_red, null) )
        }
        adapter.notifyDataSetChanged()
        //ResourcesCompat.getColor(getResources(), R.color.selected_blue, null)
        //vh.itemView.background.setTint(Color.BLUE)

        //myDataset.removeAt(vh.adapterPosition)
        //adapter.notifyDataSetChanged()
        Log.d(TAG, "on recycel click ${pos}")
    }
    private fun itemLongClick(vh: ItemAdapter.ItemViewHolder){
        if(historyViewModel.selecting==false)
            return
        val pos  = vh.adapterPosition
        //myDataset.removeAt(vh.adapterPosition)
        adapter.notifyDataSetChanged()
        Log.d(TAG, "looooong")
    }

    private fun onItemClicked(h: History){
        Log.d(TAG, "clicked onItemClicked")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //list of data

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCookieHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recycleView
        setButtons()
        var count1 = 0
        val cookieHistoryList = historyViewModel.fullHistory() //lsit of elemts of type History
//        lifecycle.coroutineScope.launch {
//            //called on every new value
//            viewModel.fullHistory().collect {
//                it -> Log.d(TAG, "fullHistory: ${it}")
//            }
//        }
        historyViewModel.addNewItem(5,100)
        historyViewModel.addNewItem(3,600)
        historyViewModel.addNewItem(0,640)
        historyViewModel.addNewItem(8,6000)

        adapter = ItemAdapter(::itemLongClick, ::onItemClicked)
        recyclerView.adapter = adapter
        //only used to improve performance
        recyclerView.setHasFixedSize(true)

        //notify observers that data has changed
        adapter.notifyDataSetChanged()

        //setupTracker()
        binding.selectButton.setOnClickListener {
            Log.d(TAG, "selectbutton")
            historyViewModel.selecting = !historyViewModel.selecting
            if(historyViewModel.selecting)
                historyViewModel.selectingList.clear()
            adapter.notifyDataSetChanged()
            setButtons()

        }
        binding.deleteButton.setOnClickListener {
            var count = 0
            historyViewModel.selectingList.sortDescending()
            while(count < historyViewModel.selectingList.size){
                //remove largest indexes from the dateset first so that the smaller indexes are not affected
                Log.d(TAG, "viewModel.selectingList: ${historyViewModel.selectingList.get(count)}")
                count++
            }
            while(!historyViewModel.selectingList.isEmpty()){
                //remove largest indexes from the dateset first so that the smaller indexes are not affected
                //myDataset.removeAt(viewModel.selectingList.removeFirst())
            }
            adapter.notifyDataSetChanged()
        }
        binding.deleteAllButton.setOnClickListener {
            deleteAllVerificationDialog()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun deleteAll(){
        //delete all data
        //myDataset.clear()
        historyViewModel.selectingList.clear()
        adapter.notifyDataSetChanged()
    }
    private fun deleteAllVerificationDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.dialog_theme)
            .setTitle(getString(R.string.delete_all_check))
            .setMessage(getString(R.string.delete_all_check_message))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                //do nothing
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteAll()
            }
            .show()
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
        if(historyViewModel.selecting){
            binding.deleteButton.setEnabled(true)
            binding.deleteAllButton.setEnabled(false)
            //recyclerView.isClickable = true
            binding.selectButton.setText(R.string.unselect)
        }else{
            binding.deleteButton.setEnabled(false)
            binding.deleteAllButton.setEnabled(true)
            //recyclerView.isClickable = false
            binding.selectButton.setText(R.string.select)
            //unselect all items
        }

    }

}