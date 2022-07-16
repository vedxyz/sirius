package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.AcademicCalendarAdapter
import xyz.vedat.sirius.viewmodels.AcademicCalendarViewModel

class AcademicCalendarFragment : Fragment(R.layout.fragment_academic_calendar) {
    private val viewModel: AcademicCalendarViewModel by activityViewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.academicCalendar == null)
            viewModel.fetch()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.academic_calendar_swipelayout)
        recyclerView = view.findViewById(R.id.academic_calendar_recyclerview)

        val adapter = AcademicCalendarAdapter()
        recyclerView.adapter = adapter

        mainContent.setOnRefreshListener {
            viewModel.fetch()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.academicCalendar == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        mainContent.isRefreshing = false

                        adapter.submitList(it.academicCalendar!!.items)
                    }
                }
            }
        }
    }
}
