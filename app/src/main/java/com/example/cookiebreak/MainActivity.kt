package com.example.cookiebreak

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.cookiebreak.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

private lateinit var binding: ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        //getWindow().getDecorView().background = ContextCompat.getDrawable(this,R.drawable.tiles_background)
        super.onCreate(savedInstanceState)
        preferencesSetup()
        val nightMode = AppCompatDelegate.getDefaultNightMode();
        Log.d("MainActivity", "Main: ${nightMode}")
        //getWindow().getDecorView().setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.background_brown, null));
        //set night mode to what
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //ensures action bar (app bar) buttons arevisibel
        setupActionBarWithNavController(this, navController)
//        val nightMode = AppCompatDelegate.getDefaultNightMode();

//        this.resources.configuration.isNightModeActive.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if(this.resources.configuration.isNightModeActive){
//
//            }
//
//        }

        Log.d("MainActivity", "main oncreate")
    }

    fun preferencesSetup(){
        //MODE_PRIVATE: created file can only be accessed by the calling application
        preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        //preferences = getPreferences(MODE_PRIVATE)
        Log.d("MainActivity", "${preferences.getInt("night_mode",0)}")

        when (preferences.getInt("night_mode",0)){
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.settings_item){
            val intent = Intent(this, SettingsActivity::class.java)
            //startService(intent);
            startActivity(intent);
            //onNightModeChanged()
            Log.d("MainActivity", "onOptionsItemSelected")
            return true
        }
        return false
    }

    //allows you to handle the up button
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//
//    private fun isDarkModeOn(): Boolean {
//        val currentNightMode = resources.configuration.uiMode and  Configuration.UI_MODE_NIGHT_MASK
//        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
//    }
}