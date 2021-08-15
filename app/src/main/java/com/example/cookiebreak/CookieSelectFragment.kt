package com.example.cookiebreak

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.cookiebreak.database.CookieBreakApplication
import com.example.cookiebreak.databinding.FragmentCookieSelectBinding
import com.example.cookiebreak.model.EatenCookiesModel
import com.example.cookiebreak.model.EatenCookiesModelFactory
import com.google.android.material.transition.MaterialElevationScale


class CookieSelectFragment : Fragment() {

    private var _binding: FragmentCookieSelectBinding? = null
    private val binding get() = _binding!!

    private var randomInt: Int = 0
    private var buttons: Int = 0

    private val viewModel: EatenCookiesModel by activityViewModels{
        EatenCookiesModelFactory(
            (activity?.application as CookieBreakApplication).database.scheduleDao()
        )
    }

    companion object {
        const val TAG = "MyActivity"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCookieSelectBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.d("hello", "ww3 ${binding.cookieEatButton.width}")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        randomInt = viewModel.randomInt
        buttons = viewModel.buttons
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.fab.shrink()
        } else {
            // code for landscape mode
            binding.fab.extend()
        }

        cookieShow(randomInt)
        setButtons()

        val preferences = this.activity?.getSharedPreferences("UserPreferences", AppCompatActivity.MODE_PRIVATE)
        val audio:Boolean = preferences?.getBoolean("audio", true)!!
        //button listeners
        binding.cookieButton.setOnClickListener {
            cookiePortion()
            togglebuttons()
        }
        binding.cookieDontEatButton.addOnLayoutChangeListener { view, i, i2, i3, i4, i5, i6, i7, i8 ->
            if(binding.cookieDontEatButton.width != 0){
                binding.cookieEatButton.width = binding.cookieDontEatButton.width
            }
        }
        binding.cookieEatButton.setOnClickListener {
            Log.d(TAG, "eat")
            //monster bite audio by LucasDuff
            //licensed under Creative Commons 0 License
            //https://freesound.org/people/LucasDuff/sounds/467701/
            if(audio){
                val mediaPlayer = MediaPlayer.create(context, R.raw.lucasduff__monster_bite_cut)
                mediaPlayer.start()
            }
            viewModel.addNewItem(randomInt)
            //set to eaten cookie vector
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
            //causes this fragment to grow/shrink during the transition
            this.apply {
                exitTransition = MaterialElevationScale(false).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
            }
            val action = CookieSelectFragmentDirections.actionCookieSelectFragmentToCookieHistoryFragment()
            view.findNavController().navigate(action)
        }

    }

    private fun setButtons(){
        binding.root.context.resources
        if (buttons == 1) {
            binding.cookieButton.visibility = View.GONE
            binding.cookieEatButton.visibility = View.VISIBLE
            binding.cookieDontEatButton.visibility = View.VISIBLE
        } else {
            binding.cookieButton.visibility = View.VISIBLE
            binding.cookieEatButton.visibility = View.GONE
            binding.cookieDontEatButton.visibility = View.GONE
        }
    }

    //toggles between selecting a button portion and choosing whether to eat button configurations
    private fun togglebuttons() {
        buttons = 1 - buttons
        setButtons()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //store variables before view is destroyed
        viewModel.randomInt=randomInt
        viewModel.buttons=buttons
        _binding = null
    }

    private fun cookiePortion() {
        randomInt = (0..8).random()
        cookieShow(randomInt)
    }

    //set UI given the generated random integer
    @SuppressLint("SetTextI18n")
    private fun cookieShow(randomInt: Int) {
        binding.cookieText.text = when (randomInt) {
            0 -> resources.getString(R.string.no_lower_case)
            1 -> "1/8"
            2 -> resources.getString(R.string.quarter)
            3 -> "3/8"
            4 -> resources.getString(R.string.half)
            5 -> "5/8"
            6 -> resources.getString(R.string.three_quarter)
            7 -> "7/8"
            8 -> resources.getString(R.string.full)
            else -> resources.getString(R.string.eaten)
        } + resources.getString(R.string.cookie_end)
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