package xyz.vedat.sirius.fragments.authenticated

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.math.MathUtils
import com.google.android.material.navigation.NavigationView
import xyz.vedat.sirius.R
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.fragments.mainNavController
import kotlin.math.max
import kotlin.math.min

class SRSHubFragment : Fragment(R.layout.fragment_srs_hub) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(defaultLogTag, "SRS hub view created")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation(view)
    }

    private fun setupNavigation(view: View) {
        val navigationView = view.findViewById<NavigationView>(R.id.srs_navigation_view)
        val appBar = view.findViewById<BottomAppBar>(R.id.srs_bottom_app_bar)
        val scrim = view.findViewById<FrameLayout>(R.id.srs_scrim)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.srs_nav_host_fragment) as NavHostFragment

        navigationView.setupWithNavController(navHostFragment.navController)
        navigationView.menu.findItem(R.id.misc_item).setOnMenuItemClickListener {
            mainNavController.popBackStack()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(navigationView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        appBar.setNavigationOnClickListener {
            scrim.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        scrim.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            scrim.visibility = View.GONE
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val baseColor = Color.BLACK
                val baseAlpha = 0.6f //ResourcesCompat.getFloat(resources, R.dimen.material_emphasis_medium)
                // Map slideOffset from [-1.0, 1.0] to [0.0, 1.0]
                val offset = (max(slideOffset, -1f) - (-1f)) / (1f - (-1f)) * (1f - 0f) + 0f
                val alpha = MathUtils.lerp(0f, 255f, offset * baseAlpha).toInt()
                scrim.setBackgroundColor(Color.argb(alpha, baseColor.red, baseColor.green, baseColor.blue))
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
    }
}
