package com.example.cookiebreak

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.cookiebreak.database.CookieBreakApplication
import com.example.cookiebreak.databinding.FragmentCookieSelectBinding
import com.example.cookiebreak.model.CookieBreakModel
import com.example.cookiebreak.model.EatenCookiesModel
import com.example.cookiebreak.model.EatenCookiesModelFactory


class CookieSelectFragment : Fragment() {

    private var _binding: FragmentCookieSelectBinding? = null
    private val binding get() = _binding!!
    //intereadsitng, having the simple layout tag around my xml allowed the datbinding to work
    //The lateinit keyword promises the Kotlin compiler that the variable will be initialized before the code calls any operations on it
    var randomInt: Int = 0
    var buttons: Int = 0

    //instance scoped to the current fragment
    private val viewModel: CookieBreakModel by viewModels()

    private val historyViewModel: EatenCookiesModel by activityViewModels{
        EatenCookiesModelFactory(
            (activity?.application as CookieBreakApplication).database.scheduleDao()
        )//diff?
    }

    companion object {
        const val TAG = "MyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCookieSelectBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        randomInt = viewModel.randomInt
        buttons = viewModel.buttons
        cookieShow(randomInt)

        //button listeners
        binding.cookieButton.setOnClickListener {
            cookiePortion()
            togglebuttons()
        }
        binding.cookieEatButton.setOnClickListener {
            Log.d(TAG, "eat")
            historyViewModel.addNewItem(randomInt,100)
            togglebuttons()
        }
        binding.cookieDontEatButton.setOnClickListener {
            Log.d(TAG, "dont eat")
            togglebuttons()
        }
        binding.fab.setOnClickListener {
            Log.d(TAG, "fab")
            //  val intent = Intent(this, HistoryActivity::class.java)
            //startService(intent);
            // startActivity(intent);
            val action = CookieSelectFragmentDirections.actionCookieSelectFragmentToCookieHistoryFragment()
            view.findNavController().navigate(action)
        }
    }

    //toggles between selecting a button portion and choosing whether to eat button configurations
    private fun togglebuttons() {
        if (buttons == 0) {
            binding.cookieButton.setVisibility(View.GONE)
            binding.cookieEatButton.setVisibility(View.VISIBLE)
            binding.cookieDontEatButton.setVisibility(View.VISIBLE)
            buttons = 1
        } else {
            binding.cookieButton.setVisibility(View.VISIBLE)
            binding.cookieEatButton.setVisibility(View.GONE)
            binding.cookieDontEatButton.setVisibility(View.GONE)
            buttons = 0
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cookiePortion() {
        randomInt = (0..8).random()
        cookieShow(randomInt);
    }

    private fun cookieShow(randomInt: Int) {
        binding.cookieText.text = when (randomInt) {
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
        val drawableResource = when (randomInt) {
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

        binding.cookieImageView.setImageResource(drawableResource)
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.randomInt=randomInt
        viewModel.buttons=buttons
        super.onSaveInstanceState(outState)
    }
}