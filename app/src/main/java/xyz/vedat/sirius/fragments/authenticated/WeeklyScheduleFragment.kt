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
import xyz.vedat.sirius.adapters.WeeklySchedulePagerAdapter
import xyz.vedat.sirius.disableViewWhileScrolling
import xyz.vedat.sirius.viewmodels.WeeklyScheduleViewModel
import java.time.LocalDate

private val DAYS_OF_WEEK = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

class WeeklyScheduleFragment : Fragment(R.layout.fragment_weekly_schedule) {
    private val viewModel: WeeklyScheduleViewModel by viewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.weeklySchedule == null) viewModel.fetch()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.weekly_schedule_main_content)
        swipeRefreshLayout = view.findViewById(R.id.weekly_schedule_swipelayout)
        tabLayout = view.findViewById(R.id.weekly_schedule_tablayout)
        viewPager = view.findViewById(R.id.weekly_schedule_view_pager)

        val pagerAdapter = WeeklySchedulePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = DAYS_OF_WEEK[position]
        }.attach()

        viewPager.disableViewWhileScrolling(swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetch()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.weeklySchedule == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false

                        pagerAdapter.submitList(it.weeklySchedule!!.days)

                        viewPager.setCurrentItem(LocalDate.now().dayOfWeek.value - 1, true)
                    }
                }
            }
        }
    }
}
