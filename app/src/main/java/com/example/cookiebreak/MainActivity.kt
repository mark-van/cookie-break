package com.example.cookiebreak

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.cookiebreak.databinding.ActivityMainBinding
import com.example.cookiebreak.model.CookieBreakModel
import androidx.activity.viewModels

private lateinit var binding: ActivityMainBinding


class MainActivity : AppCompatActivity() {
    //intereadsitng, having the simple layout tag around my xml allowed the datbinding to work
    //The lateinit keyword promises the Kotlin compiler that the variable will be initialized before the code calls any operations on it
    var randomInt: Int = 0
    var buttons: Int = 0

    private val viewModel: CookieBreakModel by viewModels()

    companion object {
        //look this string up in state to find randomInt value
        const val randomIntStr = "random int"
        const val bonttonConfig = "button config"
        const val TAG = "MyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        if (savedInstanceState != null) {
//            randomInt = savedInstanceState.getInt(randomIntStr)
//            buttons = savedInstanceState.getInt(bonttonConfig)
//            Log.d(TAG, "oncreate ${randomInt}")
//            //Show cookie
//            cookieShow(randomInt)
//        }
        randomInt = viewModel.randomInt
        buttons = viewModel.buttons
        cookieShow(randomInt)
        // Get the Button view from the layout and assign a click
        // listener to it.
        getWindow().getDecorView().setBackgroundColor(Color.rgb(221, 161, 94));

        //button listeners
        binding.cookieButton.setOnClickListener {
            cookiePortion()
            togglebuttons()
        }
        binding.cookieEatButton.setOnClickListener {
            Log.d(TAG, "eat")
            togglebuttons()
        }
        binding.cookieDontEatButton.setOnClickListener {
            Log.d(TAG, "dont eat")
            togglebuttons()
        }
        binding.fab.setOnClickListener {
            Log.d(TAG, "fab")
            val intent = Intent(this, HistoryActivity::class.java)
            //startService(intent);
            startActivity(intent);
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
//        // Save the random
//        //Log.d(TAG,"onSaveInstanceState ${randomInt}")
//        outState.putInt(randomIntStr, randomInt)
//        outState.putInt(bonttonConfig, buttons)
        viewModel.randomInt=randomInt
        viewModel.buttons=buttons
        super.onSaveInstanceState(outState)
    }
}