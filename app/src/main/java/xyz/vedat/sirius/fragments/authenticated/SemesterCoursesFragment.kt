package xyz.vedat.sirius.fragments.authenticated

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.SemesterCoursesAdapter
import xyz.vedat.sirius.viewmodels.SemesterCoursesViewModel

class SemesterCoursesFragment : Fragment(R.layout.fragment_semester_courses) {
    private val viewModel: SemesterCoursesViewModel by viewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.semesterCourses == null) viewModel.fetch()
        if (viewModel.uiState.value.semesterSelectorOptions == null) viewModel.fetchSemesterSelectorOptions()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.semester_courses_swipelayout)
        recyclerView = view.findViewById(R.id.semester_courses_recyclerview)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        val adapter = SemesterCoursesAdapter()
        recyclerView.adapter = adapter

        mainContent.setOnRefreshListener {
            viewModel.fetchWithCurrent()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.semesterCourses == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        mainContent.isRefreshing = false

                        adapter.submitList(it.semesterCourses!!.courses)
                    }
                }
            }
        }
    }
}
