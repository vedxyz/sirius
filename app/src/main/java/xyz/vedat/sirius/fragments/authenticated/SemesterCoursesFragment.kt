package xyz.vedat.sirius.fragments.authenticated

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import srs.data.Semester
import srs.data.SemesterType
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.SemesterCoursesAdapter
import xyz.vedat.sirius.viewmodels.SemesterCoursesViewModel

class SemesterCoursesFragment : Fragment(R.layout.fragment_semester_courses) {
    private val viewModel: SemesterCoursesViewModel by activityViewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var yearTil: TextInputLayout
    private lateinit var seasonTil: TextInputLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.uiState.value.semesterCourses == null)
            viewModel.fetch()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.semester_courses_swipelayout)
        recyclerView = view.findViewById(R.id.semester_courses_recyclerview)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))

        val adapter = SemesterCoursesAdapter()
        recyclerView.adapter = adapter

        mainContent.setOnRefreshListener {
            fetchWithCurrent()
        }

        yearTil = view.findViewById(R.id.semester_courses_year_til)
        seasonTil = view.findViewById(R.id.semester_courses_season_til)

        getDropdownView(yearTil)?.setOnItemClickListener { _, _, _, _ -> fetchWithCurrent() }
        getDropdownView(seasonTil)?.setOnItemClickListener { _, _, _, _ -> fetchWithCurrent() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.semesterCourses == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }

                        yearTil.isEnabled = false
                        seasonTil.isEnabled = false
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        mainContent.isRefreshing = false

                        yearTil.isEnabled = true
                        seasonTil.isEnabled = true

                        // FIXME: Use either the start date on the transcript, or the cohort in information card to set this dynamically
                        // Selecting invalid semesters (i.e., both empty and past) cause an uncaught exception as is
                        getDropdownView(yearTil)?.setSimpleItems(
                            arrayOf(
                                "2022",
                                "2021",
                                "2020",
                                "2019"
                            )
                        )
                        getDropdownView(seasonTil)?.setSimpleItems(
                            listOf(
                                SemesterType.Fall,
                                SemesterType.Spring,
                                SemesterType.Summer
                            ).map { e -> e.toString() }.toTypedArray()
                        )
                        getDropdownView(yearTil)?.setText(it.semesterCourses!!.semester.year, false)
                        getDropdownView(seasonTil)?.setText(it.semesterCourses!!.semester.season.toString(), false)

                        adapter.submitList(it.semesterCourses!!.courses)
                    }
                }
            }
        }
    }

    private fun fetchWithCurrent() {
        if (!yearTil.isEnabled || !seasonTil.isEnabled)
            return

        viewModel.fetch(
            Semester(
                yearTil.editText!!.text.toString(),
                SemesterType.valueOf(seasonTil.editText!!.text.toString())
            )
        )
    }

    private fun getDropdownView(til: TextInputLayout) = til.editText as? MaterialAutoCompleteTextView
}
