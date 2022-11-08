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
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.CourseGradesPagerAdapter
import xyz.vedat.sirius.viewmodels.GradesViewModel

class GradesFragment : Fragment(R.layout.fragment_grade) {
    private val viewModel: GradesViewModel by viewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.grades == null) viewModel.fetch()
        if (viewModel.uiState.value.semesterSelectorOptions == null) viewModel.fetchSemesterSelectorOptions()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.grades_main_content)
        swipeRefreshLayout = view.findViewById(R.id.grades_swipelayout)
        tabLayout = view.findViewById(R.id.grades_tablayout)
        viewPager = view.findViewById(R.id.grade_view_pager)

        val pagerAdapter = CourseGradesPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = viewModel.uiState.value.grades!![position].title
        }.attach()

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                swipeRefreshLayout.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchWithCurrent()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.grades == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        swipeRefreshLayout.isRefreshing = false

                        pagerAdapter.submitList(it.grades!!)
                    }
                }
            }
        }
    }
}
