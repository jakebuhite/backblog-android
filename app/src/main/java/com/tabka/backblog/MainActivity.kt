package com.tabka.backblog

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.tabka.backblog.databinding.ActivityMainBinding
import com.tabka.backblog.repos.local_storage.LogsLocalRepo

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Repo for local storage calls
        LogsLocalRepo.init(this)

        // TESTING
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        // Make the top bar of the android screen transparent
        window.apply {
            statusBarColor = android.graphics.Color.TRANSPARENT
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the navigation bar
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val title = when (destination.id) {
                R.id.navigation_home -> getString(R.string.title_home)
                R.id.navigation_search -> getString(R.string.title_search)
                else -> ""
            }
            binding.toolbarTitle.text = title
        }
    }

}