package xyz.vedat.sirius.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.data.Semester
import srs.data.SemesterCourses
import xyz.vedat.sirius.SessionManager
import xyz.vedat.sirius.fragments.authenticated.components.ISemesterSelectorUiState
import xyz.vedat.sirius.fragments.authenticated.components.ISemesterSelectorViewModel

class SemesterCoursesViewModel : ViewModel(), ISemesterSelectorViewModel {
    data class UiState(
        val isLoading: Boolean = true,
        val semesterCourses: SemesterCourses? = null,
        override val isSemesterSelectorLoading: Boolean = true,
        override val semesterSelectorOptions: List<Semester>? = null,
    ) : ISemesterSelectorUiState {
        override val semesterSelectorSelection: Semester?
            get() = semesterCourses?.semester
    }

    private val _uiState = MutableStateFlow(UiState())
    override val uiState = _uiState.asStateFlow()

    fun fetch(semester: Semester? = null) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getSemesterCourses(semester) ?: throw Exception("No session")
            }

            _uiState.update {
                it.copy(isLoading = false, semesterCourses = result)
            }
        }
    }

    fun fetchWithCurrent() {
        fetch(uiState.value.semesterSelectorSelection)
    }

    override fun fetchSemesterSelectorOptions() {
        _uiState.update {
            it.copy(isSemesterSelectorLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getSemesters() ?: throw Exception("No session")
            }

            _uiState.update {
                it.copy(isSemesterSelectorLoading = false, semesterSelectorOptions = result)
            }
        }
    }

    override fun onSemesterSelectorSelected(semester: Semester) {
        fetch(semester)
    }
}
