package com.markvangenderen.cookiebreak

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.markvangenderen.cookiebreak.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = preferences.edit() //remember to commit edits
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        overridePendingTransition(R.anim.slide_down_from_top,R.anim.slide_down_from_center) //lets have settings slide in and out from top
        setContentView(binding.root)

        //set initial checked values
        val checked = when (preferences.getInt("night_mode",0)){
            0 -> R.id.radio_night_mode_system
            1 -> R.id.radio_night_mode_off
            else -> R.id.radio_night_mode_on
        }
        binding.radioNightModeGroup.check(checked)
        binding.audioSwitch.isChecked = preferences.getBoolean("audio", true)

        //ui listeners
        binding.audioSwitch.setOnCheckedChangeListener { compoundButton, c ->
            if (c){
                //checked
                editor.putBoolean("audio",true)
                editor.apply()
            }else{
                editor.putBoolean("audio",false)
                editor.apply()
            }
        }

        binding.radioNightModeSystem.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            editor.putInt("night_mode", 0)
            editor.apply()
        }
        binding.radioNightModeOff.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor.putInt("night_mode", 1)
            editor.apply()
        }
        binding.radioNightModeOn.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor.putInt("night_mode", 2)
            editor.apply()
        }


    }

}