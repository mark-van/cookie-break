package com.example.cookiebreak

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.example.cookiebreak.adapter.ItemAdapter
import com.example.cookiebreak.database.CookieBreakApplication
import com.example.cookiebreak.database.History
import com.example.cookiebreak.databinding.FragmentCookieHistoryBinding
import com.example.cookiebreak.model.EatenCookiesModel
import com.example.cookiebreak.model.EatenCookiesModelFactory
import com.example.cookiebreak.model.posIdPair
import com.example.cookiebreak.util.DividerItemDecorationLastExcluded
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform


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
        )
    }
    private lateinit var adapter: ItemAdapter

    private fun itemLongClick(vh: ItemAdapter.ItemViewHolder){
//        if(historyViewModel.select.value==false)
//            return
//        val pos  = vh.adapterPosition
//        //myDataset.removeAt(vh.adapterPosition)
//        adapter.notifyDataSetChanged()
//        Log.d(TAG, "looooong")
    }


    private fun onItemClicked(vh: ItemAdapter.ItemViewHolder, h: History){
        Log.d(TAG, "clicked onItemClicked")

        val pos  = vh.adapterPosition
        val card: MaterialCardView = vh.itemView as MaterialCardView
        //toggle entry from list
        var index =contains(pos)
        Log.d(TAG, "clicked item pos ${pos}")
        Log.d(TAG, "clicked item index in list ${index}")
        if(index != -1) {
            historyViewModel.selectList.removeAt(index)
            Log.d(TAG, historyViewModel.selectList.toString())
            //card.strokeColor = ResourcesCompat.getColor(getResources(), R.color.selected_blue, null)
            //card.strokeWidth = getResources().getDimension(R.dimen.ten).toInt()
            card.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white_selectable, null))
        }else {
            Log.d(TAG, historyViewModel.selectList.toString())
            historyViewModel.selectList.add(posIdPair(pos,h.id));
            //card.strokeColor = ResourcesCompat.getColor(getResources(), R.color.selected_red, null)
            //card.strokeWidth = getResources().getDimension(R.dimen.ten).toInt()
            card.setCardBackgroundColor(
                ResourcesCompat.getColor(getResources(), R.color.selected_red, null) )
        }
        //ResourcesCompat.getColor(getResources(), R.color.selected_blue, null)
        //vh.itemView.background.setTint(Color.BLUE)

        //myDataset.removeAt(vh.adapterPosition)
        //adapter.notifyDataSetChanged()
//        Log.d(TAG, "on recycel click ${pos}")
    }
    //returns the elents index if in the list, otherwise -1

    fun printList(){
        var count = 0
        while(historyViewModel.selectList.size>0){
        }
    }

    fun contains(pos: Int):Int{
        var count = 0
        while(historyViewModel.selectList.size>count){
            if(historyViewModel.selectList.get(count).pos == pos)
                return count
            count++
        }
        return -1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    fun manageLayout(){
        val widthDp = resources.displayMetrics.run { widthPixels / density }
        Log.d(TAG, "${widthDp}")
        val orientation = this.resources.configuration.orientation
        if ((widthDp>=600 && orientation == Configuration.ORIENTATION_PORTRAIT) || (widthDp>=900 && orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            binding.recycleView.layoutManager = GridLayoutManager(this.context, 2, RecyclerView.VERTICAL , false)
        }else{
            binding.recycleView.layoutManager = LinearLayoutManager(this.context,
                LinearLayoutManager.VERTICAL, true)
            (binding.recycleView.layoutManager as LinearLayoutManager).stackFromEnd = true
            binding.recycleView.addItemDecoration(
                DividerItemDecorationLastExcluded(ContextCompat.getDrawable(requireContext(), R.drawable.ic_cookie_row)!!)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //reverse order(have most recent on top)
        manageLayout()

    //    binding.recycleView.layoutManager = GridLayoutManager(this.context, 2, RecyclerView.VERTICAL , true)
      //  (binding.recycleView.layoutManager as GridLayoutManager).
//        binding.recycleView.layoutManager = LinearLayoutManager(this.context,
//            LinearLayoutManager.VERTICAL, true)
//        (binding.recycleView.layoutManager as LinearLayoutManager).stackFromEnd = true
//        binding.recycleView.addItemDecoration(
//            DividerItemDecorationLastExcluded(ContextCompat.getDrawable(requireContext(), R.drawable.ic_cookie_row)!!)
//        )
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab)
            endView = binding.historyLayout
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT //Color.argb(67,170,68,0)
                //ResourcesCompat.getColor(getResources(), R.color.dark_brown, null)
            containerColor = Color.TRANSPARENT
            startContainerColor = ResourcesCompat.getColor(getResources(), R.color.dark_brown, null)
            endContainerColor = ResourcesCompat.getColor(getResources(), R.color.background_brown, null)

        }
        returnTransition = Slide().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_medium).toLong()
            addTarget(R.id.history_layout)
        }
//        historyViewModel.addNewItem(5,100)
//        historyViewModel.addNewItem(3,600)
//        historyViewModel.addNewItem(0,640)
//        historyViewModel.addNewItem(8,6000)

        adapter = ItemAdapter(::itemLongClick, ::onItemClicked, ::setEffects)
        binding.recycleView.adapter = adapter

//        lifecycle.coroutineScope.launch {
//            historyViewModel.fullHistory().collect {
//                adapter.submitList(it)
//            }
//        }
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        //much faster than above
        historyViewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
                //Log.d(TAG, "allItems.observe ${it.isEmpty()}")
                //if there are no items
                if(it.isEmpty()){
                    binding.linearLayout.visibility = View.GONE
                    binding.noItemsLayout.visibility = View.VISIBLE
                }else{
                    binding.linearLayout.visibility = View.VISIBLE
                    binding.noItemsLayout.visibility = View.GONE
                }
            }
        }
        setButtons()


        //only used to improve performance
        //recyclerView.setHasFixedSize(true)

        //notify observers that data has changed
        //adapter.notifyDataSetChanged()//??


        binding.selectButton.setOnClickListener {
            Log.d(TAG, "selectbutton")
            historyViewModel.select.value = !historyViewModel.select?.value!!
            if(historyViewModel.select.value!!)
                historyViewModel.selectList.clear()
            adapter.notifyDataSetChanged()
            setButtons()

        }
        binding.deleteButton.setOnClickListener {
            var count = 0

            val size = historyViewModel.selectList.size!!
            while(count < size){
                //Log.d(TAG, "viewModel.selectList: ${historyViewModel.selectList.get(count)}")
                count++
                historyViewModel.deleteHistory(historyViewModel.selectList.removeLast()?.id!!)
            }

            if(count == 0){
                Snackbar.make(
                    binding.recycleView,
                    "please select cookies to delete",
                    Snackbar.LENGTH_SHORT
                ).show()
            }else{
                //make snack bar
                val plural: String
                if (count>1)
                    plural = "s"
                else
                    plural = ""
                Snackbar.make(
                    binding.recycleView,
                    "deleted ${count} cookie${plural}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }


            //adapter.notifyDataSetChanged()
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
        Log.d(TAG, "deleteAll ${historyViewModel.selectList}")
        historyViewModel.deleteAll()
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


    private fun ontouch(): Boolean {
        return true
    }
    private fun setButtons() {
        if(historyViewModel.select.value==true){
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

    fun setEffects(vh: ItemAdapter.ItemViewHolder){
        if(historyViewModel.select.value == false){
            //not clickable, and no sound effects
            vh.itemView.isClickable = false
            vh.itemView.isSoundEffectsEnabled = false
        }
        else{
            vh.itemView.isClickable = true
            vh.itemView.isSoundEffectsEnabled = true
        }
        val card: MaterialCardView = vh.itemView as MaterialCardView
        if(historyViewModel.select.value==true){
            if(contains(vh.adapterPosition) != -1) {
                card.setCardBackgroundColor(Color.rgb(255,68,68))//red
               // card.strokeColor = ResourcesCompat.getColor(getResources(), R.color.selected_red, null)
                //card.strokeWidth = getResources().getDimension(R.dimen.ten).toInt()
            }else {
                card.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white_selectable, null))//blue
                //card.setCardBackgroundColor(Color.WHITE)
                //card.strokeWidth = 50 //dipToPixels(10f).toInt() //getResources().getDimension(R.dimen.ten).toInt()
                Log.d(TAG, "deleteAll ${card.strokeWidth}")
                //card.strokeColor = ResourcesCompat.getColor(getResources(), R.color.selected_blue, null)
            }
        }else{
            card.setCardBackgroundColor(Color.WHITE)
            //card.strokeWidth = 50  //dipToPixels(10f).toInt() //getResources().getDimension(R.dimen.ten).toInt()
            Log.d(TAG, "deleteAll ${card.strokeWidth}")
            //card.strokeColor = ResourcesCompat.getColor(getResources(), R.color.dark_brown, null)
        }
    }
    fun dipToPixels(dipValue: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, resources.displayMetrics)


}