package com.toyota.showcase

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.toyota.showcase.ui.activities.SearchActivity

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val KEY_NIGHT_MODE = "night_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        setupNavigation()
        showWelcomeDialog()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun applyTheme() {
        val preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isNightMode = preferences.getBoolean(KEY_NIGHT_MODE, false)
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        updateThemeIcon(menu.findItem(R.id.action_toggle_theme))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            R.id.action_toggle_theme -> {
                toggleDarkMode()
                updateThemeIcon(item)
                true
            }
            else -> {
                // Let the Navigation component handle the menu item selection
                item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
            }
        }
    }

    private fun toggleDarkMode() {
        val preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isCurrentlyNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        
        preferences.edit().putBoolean(KEY_NIGHT_MODE, !isCurrentlyNightMode).apply()
        
        AppCompatDelegate.setDefaultNightMode(
            if (isCurrentlyNightMode) AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    private fun updateThemeIcon(menuItem: MenuItem) {
        val isNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        menuItem.setIcon(
            if (isNightMode) R.drawable.ic_light_mode
            else R.drawable.ic_dark_mode
        )
    }

    private fun showWelcomeDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.welcome_title)
            .setMessage(R.string.welcome_message)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
} 