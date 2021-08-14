package com.example.cookiebreak

import android.annotation.SuppressLint
import android.app.UiModeManager.MODE_NIGHT_AUTO
import android.app.UiModeManager.MODE_NIGHT_NO
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.cookiebreak.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = preferences.edit() //remember to commit edits
        var binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val checked = when (preferences.getInt("night_mode",0)){
            0 -> R.id.radio_night_mode_system
            1 -> R.id.radio_night_mode_off
            else -> R.id.radio_night_mode_on
        }
        binding.radioNightModeGroup.check(checked)
        binding.audioSwitch.isChecked = preferences.getBoolean("audio", true)
        Log.d("MainActivity", "switchpref: ${preferences.getBoolean("audio", true)}")
        Log.d("MainActivity", "switch: ${binding.audioSwitch.isChecked}")
        binding.audioSwitch.setOnCheckedChangeListener { compoundButton, c ->
            if (c){
                //checked
                editor.putBoolean("audio",true)
                editor.apply();
            }else{
                editor.putBoolean("audio",false)
                editor.apply();
            }
        }
//        binding.radioNightModeGroup.setOnCheckedChangeListener { radioGroup, id ->
//             run {
//                 Log.d("MainActivity", "radioNightModeGroup")
////                 when (id) {
////                     R.id.radio_night_mode_on -> AppCompatDelegate.setDefaultNightMode(
////                         AppCompatDelegate.MODE_NIGHT_YES
////                     )
////                     R.id.radio_night_mode_off -> AppCompatDelegate.setDefaultNightMode(
////                         AppCompatDelegate.MODE_NIGHT_NO
////                     )
////                     else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
////
////                 }
//                 val nightMode = AppCompatDelegate.getDefaultNightMode();
//                 //Set the theme mode for the restarted activity
//                 if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
//                     AppCompatDelegate.setDefaultNightMode(
//                         AppCompatDelegate.MODE_NIGHT_NO
//                     )
//                 }
//                 recreate()
//             }
//        }
        binding.radioNightModeSystem.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            editor.putInt("night_mode", 0)
            editor.commit()
        }
        binding.radioNightModeOff.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor.putInt("night_mode", 1)
            editor.commit()
        }
        binding.radioNightModeOn.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor.putInt("night_mode", 2)
            editor.commit()

            Log.d("MainActivity", "settings ${preferences.getInt("night_mode",0)}")
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }



}