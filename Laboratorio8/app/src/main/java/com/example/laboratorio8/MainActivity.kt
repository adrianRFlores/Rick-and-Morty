package com.example.laboratorio8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainer_recyclerActivity
        ) as NavHostFragment
        navController = navHostFragment.navController

        val appbarConfig = AppBarConfiguration(setOf(R.id.CharactersFragment, R.id.CharacterDetailsFragment))
        topAppBar = findViewById(R.id.toolbar)
        topAppBar.setupWithNavController(navController, appbarConfig)

        setListeners()
        setNavigationChange()
    }

    private fun setListeners() {

    }

    fun getToolbar(): MaterialToolbar {
        return topAppBar
    }

    private fun setNavigationChange() {
        navController.addOnDestinationChangedListener {_, destination, _ ->
            when(destination.id) {
                R.id.CharacterDetailsFragment -> {
                    topAppBar.menu.findItem(R.id.menu_item_a_z).isVisible = false
                    topAppBar.menu.findItem(R.id.menu_item_z_a).isVisible = false
                }

                else -> {
                    topAppBar.menu.findItem(R.id.menu_item_a_z).isVisible = true
                    topAppBar.menu.findItem(R.id.menu_item_z_a).isVisible = true
                }
            }
        }
    }

}