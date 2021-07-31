package com.example.cookiebreak

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    //The lateinit keyword promises the Kotlin compiler that the variable will be initialized before the code calls any operations on it
    lateinit var cookieText: TextView
    lateinit var cookieImage: ImageView
    var randomInt: Int = 0
    companion object {
        //look this string up in state to find randomInt value
        const val randomIntStr = "random int"
        const val TAG = "MyActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cookieText = findViewById(R.id.cookie_text)
        cookieImage = findViewById(R.id.cookie_image_view)

        if (savedInstanceState != null) {
            randomInt = savedInstanceState.getInt(randomIntStr)
            Log.d(TAG,"oncreate ${randomInt}")
            //Show cookie
            cookieShow(randomInt)
        }

        // Get the Button view from the layout and assign a click
        // listener to it.
        getWindow().getDecorView().setBackgroundColor(Color.rgb(221, 161, 94));
        val cookieButton: Button = findViewById(R.id.cookie_button)
        cookieButton.setOnClickListener { cookiePortion() }
    }

    private fun cookiePortion() {
        randomInt = (0..8).random()
        cookieShow(randomInt);
    }

    private fun cookieShow(randomInt: Int){
        cookieText.text = when (randomInt) {
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

        cookieImage.setImageResource(drawableResource)
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        // Save the random
        //Log.d(TAG,"onSaveInstanceState ${randomInt}")
        outState.putInt(randomIntStr, randomInt)
        super.onSaveInstanceState(outState)
    }
}