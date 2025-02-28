package com.caller.id.app.prototype.presentation.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.CONSUMED
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.caller.id.app.prototype.R
import com.caller.id.app.prototype.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel by viewModels<MainViewModel>()

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        if (destination.id == R.id.contactsFragment || destination.id == R.id.blockListFragment) {
            binding.bottomNav.visibility = View.VISIBLE
        } else {
            binding.bottomNav.visibility = View.GONE

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isPermissionTaskRunning.value
            }
        }

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(
            window.decorView
        ) { _, insets ->
            val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            findViewById<View>(R.id.main).updatePadding(bottom = windowInsets.bottom)

            return@setOnApplyWindowInsetsListener CONSUMED
        }

       // setUpBottomNavigationBar()

    }

    override fun onResume() {
        super.onResume()
        runPermissionTask()
    }

    override fun onPause() {
        if(::navController.isInitialized){
            navController.removeOnDestinationChangedListener(listener)
        }
        super.onPause()
    }

    private fun runPermissionTask() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_CONTACTS)
            .request { allGranted, _, _ ->
                if (allGranted) {
                    setUpBottomNavigationBar()
                    viewModel.setPermissionTaskRunningFalse()

                } else {
                    goToSettings()
                }
            }
    }

    private fun goToSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = android.net.Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun setUpBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val navGraph = inflater.inflate(R.navigation.nav_graph)
        navController = navHostFragment.navController.apply {
            graph = navGraph
        }
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(listener)
    }



}