package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import xyz.vedat.sirius.R
import xyz.vedat.sirius.defaultLogTag

class AnonymousHubFragment : Fragment(R.layout.fragment_anonymous_hub) {
    private val navigationHiderIds = listOf(
        R.id.automatic_verification_navfragment,
        R.id.manual_verification_navfragment
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.v(defaultLogTag, "Anonymous hub view created")

        setupNavigation(view)
    }

    private fun setupNavigation(view: View) {
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.anonymous_bottom_navigation)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.anonymous_nav_host_fragment) as NavHostFragment

        bottomNavigationView.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (navigationHiderIds.contains(destination.id)) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}
