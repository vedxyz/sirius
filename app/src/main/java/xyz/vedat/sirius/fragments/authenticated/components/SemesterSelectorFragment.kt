package xyz.vedat.sirius.fragments.authenticated.components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import srs.data.Semester
import srs.data.SemesterType
import xyz.vedat.sirius.R
import xyz.vedat.sirius.viewmodels.GradesViewModel
import xyz.vedat.sirius.viewmodels.SemesterCoursesViewModel

interface ISemesterSelectorUiState {
    val isSemesterSelectorLoading: Boolean
    val semesterSelectorOptions: List<Semester>?
    val semesterSelectorSelection: Semester?
}

interface ISemesterSelectorViewModel {
    val uiState: StateFlow<ISemesterSelectorUiState>
    fun fetchSemesterSelectorOptions()
    fun onSemesterSelectorSelected(semester: Semester)
}

abstract class SemesterSelectorFragment : Fragment(R.layout.fragment_semester_selector) {
    abstract val viewModel: ISemesterSelectorViewModel

    private lateinit var yearTil: TextInputLayout
    private lateinit var seasonTil: TextInputLayout

    private val yearDropdown get() = yearTil.editText as MaterialAutoCompleteTextView
    private val seasonDropdown get() = seasonTil.editText as MaterialAutoCompleteTextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yearTil = view.findViewById(R.id.semester_courses_year_til)
        seasonTil = view.findViewById(R.id.semester_courses_season_til)

        yearDropdown.setOnItemClickListener { _, _, _, _ -> triggerSelection() }
        seasonDropdown.setOnItemClickListener { _, _, _, _ -> triggerSelection() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isSemesterSelectorLoading) {
                        yearTil.isEnabled = false
                        seasonTil.isEnabled = false
                    } else {
                        updateSelector(it.semesterSelectorOptions?.sorted(), it.semesterSelectorSelection)
                    }
                }
            }
        }
    }

    private fun triggerSelection() {
        var selectedSemester =
            Semester(yearDropdown.text.toString(), SemesterType.valueOf(seasonDropdown.text.toString()))
        val options = viewModel.uiState.value.semesterSelectorOptions
        if (options?.contains(selectedSemester) == false) {
            selectedSemester = options.first { it.year == selectedSemester.year }
        }

        viewModel.onSemesterSelectorSelected(selectedSemester)
    }

    private fun updateSelector(semesters: List<Semester>?, selectorSelection: Semester?) {
        yearTil.isEnabled = semesters != null
        seasonTil.isEnabled = semesters != null
        if (semesters == null) return

        yearDropdown.setSimpleItems(semesters.distinctBy { it.year }.map { it.year }.toTypedArray())

        val selection = selectorSelection ?: semesters.last()
        updateSelectorSeasonsForYear(semesters, selection.year)
        updateSelectorText(selection)
    }

    private fun updateSelectorSeasonsForYear(semesters: List<Semester>, year: String) {
        seasonDropdown.setSimpleItems(semesters.filter { it.year == year }.sortedBy { it.season }
            .map { it.season.toString() }.toTypedArray())
    }

    private fun updateSelectorText(semester: Semester) {
        yearDropdown.setText(semester.year, false)
        seasonDropdown.setText(semester.season.toString(), false)
    }
}

class SemesterCoursesSelectorFragment : SemesterSelectorFragment() {
    override val viewModel: SemesterCoursesViewModel by viewModels({ requireParentFragment() })
}

class GradesSemesterSelectorFragment : SemesterSelectorFragment() {
    override val viewModel: GradesViewModel by viewModels({ requireParentFragment() })
}
