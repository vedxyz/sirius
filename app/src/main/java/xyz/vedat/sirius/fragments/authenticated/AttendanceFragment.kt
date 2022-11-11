package xyz.vedat.sirius.fragments.authenticated

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.AttendancePagerAdapter
import xyz.vedat.sirius.disableViewWhileScrolling
import xyz.vedat.sirius.viewmodels.AttendanceViewModel

class AttendanceFragment : Fragment(R.layout.fragment_attendance) {
    private val viewModel: AttendanceViewModel by viewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.attendance == null) viewModel.fetch()
        if (viewModel.uiState.value.semesterSelectorOptions == null) viewModel.fetchSemesterSelectorOptions()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.attendance_main_content)
        swipeRefreshLayout = view.findViewById(R.id.attendance_swipelayout)
        tabLayout = view.findViewById(R.id.attendance_tablayout)
        viewPager = view.findViewById(R.id.attendance_view_pager)

        val pagerAdapter = AttendancePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewModel.uiState.value.attendance!![position].title
        }.attach()

        viewPager.disableViewWhileScrolling(swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchWithCurrent()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.attendance == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false

                        pagerAdapter.submitList(it.attendance!!)

                        if (it.shouldResetPosition) {
                            viewPager.setCurrentItem(0, true)
                            viewModel.resetPositionConsumed()
                        }
                    }
                }
            }
        }
    }
}
