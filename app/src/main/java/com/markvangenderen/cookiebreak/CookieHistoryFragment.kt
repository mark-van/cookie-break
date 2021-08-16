package com.markvangenderen.cookiebreak

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.Gravity.END
import android.view.Gravity.RIGHT
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
import com.markvangenderen.cookiebreak.adapter.ItemAdapter
import com.markvangenderen.cookiebreak.database.CookieBreakApplication
import com.markvangenderen.cookiebreak.database.History
import com.markvangenderen.cookiebreak.databinding.FragmentCookieHistoryBinding
import com.markvangenderen.cookiebreak.model.EatenCookiesModel
import com.markvangenderen.cookiebreak.model.EatenCookiesModelFactory
import com.markvangenderen.cookiebreak.model.PosIdPair
import com.markvangenderen.cookiebreak.util.DividerItemDecorationLastExcluded
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform


class CookieHistoryFragment : Fragment() {

    private var _binding: FragmentCookieHistoryBinding? = null
    private val binding get() = _binding!!


    //activityViewModels() gives you the ViewModel instance scoped to the current activity.
    //Therefore the instance will remain the same across multiple fragments in the same activity.
    private val historyViewModel: EatenCookiesModel by activityViewModels{
        EatenCookiesModelFactory(
            (activity?.application as CookieBreakApplication).database.scheduleDao()
        )
    }
    private lateinit var adapter: ItemAdapter


    private fun onItemClicked(vh: ItemAdapter.ItemViewHolder, h: History){
        val tv1 = TypedValue()
        val pos  = vh.adapterPosition
        val card: MaterialCardView = vh.itemView as MaterialCardView

        //toggle entry from list
        val index = contains(pos)
        if(index != -1) {
            historyViewModel.selectList.removeAt(index)
            context?.theme?.resolveAttribute(R.attr.cardSelectBackgroundColor, tv1, true)
            card.setCardBackgroundColor(tv1.data)
        }else {
            historyViewModel.selectList.add(PosIdPair(pos,h.id))
            context?.theme?.resolveAttribute(R.attr.cardSelectedBackgroundColor, tv1, true)
            card.setCardBackgroundColor(tv1.data)
        }
    }

    //returns the index of the first element contained in the list with the given position value,
    //otherwise -1
    fun contains(pos: Int):Int{
        var count = 0
        while(historyViewModel.selectList.size>count){
            if(historyViewModel.selectList[count].pos == pos)
                return count
            count++
        }
        return -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCookieHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    //set two span grid layout for very large screen, otherwise use linear layout
    private fun manageLayout(){
        val widthDp = resources.displayMetrics.run { widthPixels / density }
        val orientation = this.resources.configuration.orientation
        if ((widthDp>=600 && orientation == Configuration.ORIENTATION_PORTRAIT) || (widthDp>=900 && orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            binding.recycleView.layoutManager = GridLayoutManager(this.context, 2, RecyclerView.VERTICAL , false)
        }else{
            binding.recycleView.layoutManager = LinearLayoutManager(this.context,
                LinearLayoutManager.VERTICAL, true)
            (binding.recycleView.layoutManager as LinearLayoutManager).stackFromEnd = true
            binding.recycleView.addItemDecoration(
                DividerItemDecorationLastExcluded(this.requireContext(), ContextCompat.getDrawable(requireContext(), R.drawable.ic_cookie_row)!!)
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageLayout()
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab)
            duration = 300
            scrimColor = Color.TRANSPARENT
            containerColor = Color.TRANSPARENT
            startContainerColor = ResourcesCompat.getColor(resources, R.color.dark_brown, null)
            endContainerColor = ResourcesCompat.getColor(resources, R.color.background_brown, null)

        }
        returnTransition = Slide(END).apply {
            duration = 200
            addTarget(R.id.history_layout)
        }

        adapter = ItemAdapter(::onItemClicked, ::setEffects)
        binding.recycleView.adapter = adapter


        //observe changes in allItems list and update UI accordingly
        historyViewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
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

        binding.selectButton.setOnClickListener {
            historyViewModel.select.value = !historyViewModel.select.value!!
            if(historyViewModel.select.value!!)
                historyViewModel.selectList.clear()
            adapter.notifyDataSetChanged()
            setButtons()

        }
        binding.deleteButton.setOnClickListener {
            var count = 0

            val size = historyViewModel.selectList.size
            while(count < size){
                count++
                historyViewModel.deleteHistory(historyViewModel.selectList.removeLast().id)
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
                    "deleted $count cookie$plural",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

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

    //set buttons according to select value
    private fun setButtons() {
        if(historyViewModel.select.value==true){
            binding.deleteButton.isEnabled = true
            binding.deleteAllButton.isEnabled = false
            binding.selectButton.setText(R.string.unselect)
        }else{
            binding.deleteButton.isEnabled = false
            binding.deleteAllButton.isEnabled = true
            binding.selectButton.setText(R.string.select)
        }

    }

    private fun setEffects(vh: ItemAdapter.ItemViewHolder){
        val tv = TypedValue()
        if(historyViewModel.select.value == false){
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
                context?.theme?.resolveAttribute(R.attr.cardSelectedBackgroundColor, tv, true)
                card.setCardBackgroundColor(tv.data)
            }else {
                context?.theme?.resolveAttribute(R.attr.cardSelectBackgroundColor, tv, true)
                card.setCardBackgroundColor(tv.data)
            }
        }else{
            context?.theme?.resolveAttribute(R.attr.cardBGColor, tv, true)
            card.setCardBackgroundColor(tv.data)
        }
    }

}