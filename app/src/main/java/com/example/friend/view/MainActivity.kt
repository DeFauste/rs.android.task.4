package com.example.friend.view

import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.friend.R
import com.example.friend.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var title: TextView? = null
    var settingsIcon: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = binding.title
        settingsIcon = binding.settingsIcon

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

    }
}