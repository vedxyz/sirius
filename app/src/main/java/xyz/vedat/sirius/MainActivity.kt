package xyz.vedat.sirius

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val navigationHiderIds = listOf(
        R.id.automatic_verification_navfragment,
        R.id.manual_verification_navfragment
    )

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupNavigation()
    }

    override fun onBackPressed() {
        if (!navigationHiderIds.contains(navController.currentDestination?.id))
            super.onBackPressed()
    }

    private fun setupNavigation() {
        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.anonymous_bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (navigationHiderIds.contains(destination.id)) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}
