package com.example.cookiebreak

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.cookiebreak.database.CookieBreakApplication
import com.example.cookiebreak.databinding.FragmentCookieSelectBinding
import com.example.cookiebreak.model.CookieBreakModel
import com.example.cookiebreak.model.EatenCookiesModel
import com.example.cookiebreak.model.EatenCookiesModelFactory
import java.util.*


class CookieSelectFragment : Fragment() {

    private var _binding: FragmentCookieSelectBinding? = null
    private val binding get() = _binding!!
    //intereadsitng, having the simple layout tag around my xml allowed the datbinding to work
    //The lateinit keyword promises the Kotlin compiler that the variable will be initialized before the code calls any operations on it
    var randomInt: Int = 0
    var buttons: Int = 0

    //instance scoped to the current fragment
    //private val viewModel: CookieBreakModel by viewModels()

    private val viewModel: EatenCookiesModel by activityViewModels{
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        randomInt = viewModel.randomInt
        buttons = viewModel.buttons

        cookieShow(randomInt)
        setButtons()

        //button listeners
        binding.cookieButton.setOnClickListener {
            cookiePortion()
            togglebuttons()
        }
        binding.cookieEatButton.setOnClickListener {
            Log.d(TAG, "eat")
//            val current = Instant.now()
//            val zone = TimeZone.getDefault().toZoneId()
//            val clock = Clock.fixed(current, zone)
            viewModel.addNewItem(randomInt)
            randomInt = 9
            cookieShow(randomInt)
            togglebuttons()
        }
        binding.cookieDontEatButton.setOnClickListener {
            Log.d(TAG, "dont eat")
            randomInt = 0
            cookieShow(randomInt)
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

    private fun setButtons(){
        if (buttons == 1) {
            binding.cookieButton.setVisibility(View.GONE)
            binding.cookieEatButton.setVisibility(View.VISIBLE)
            binding.cookieDontEatButton.setVisibility(View.VISIBLE)
        } else {
            binding.cookieButton.setVisibility(View.VISIBLE)
            binding.cookieEatButton.setVisibility(View.GONE)
            binding.cookieDontEatButton.setVisibility(View.GONE)
        }
    }
    //toggles between selecting a button portion and choosing whether to eat button configurations
    private fun togglebuttons() {
        buttons = 1 - buttons
        setButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.randomInt=randomInt
        viewModel.buttons=buttons
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
            8 -> "full"
            else -> "eaten"
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
            8 -> R.drawable.ic_cookie_full
            else -> R.drawable.ic_cookie_eaten
        }

        binding.cookieImageView.setImageResource(drawableResource)
    }

}