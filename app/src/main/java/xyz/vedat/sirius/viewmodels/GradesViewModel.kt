package xyz.vedat.sirius.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.data.CourseGrades
import srs.data.Semester
import xyz.vedat.sirius.SessionManager
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.fragments.authenticated.components.ISemesterSelectorUiState
import xyz.vedat.sirius.fragments.authenticated.components.ISemesterSelectorViewModel

class GradesViewModel : ViewModel(), ISemesterSelectorViewModel {
    data class UiState(
        val isLoading: Boolean = true,
        val grades: List<CourseGrades>? = null,
        override val isSemesterSelectorLoading: Boolean = true,
        override val semesterSelectorOptions: List<Semester>? = null,
        override val semesterSelectorSelection: Semester? = null
    ) : ISemesterSelectorUiState

    private val _uiState = MutableStateFlow(UiState())
    override val uiState = _uiState.asStateFlow()

    fun fetch(semester: Semester? = null) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getGrades(semester) ?: throw Exception("No session")
            }
            val selectedSemester = semester ?: withContext(Dispatchers.IO) {
                SessionManager.session?.getCurrentSemester() ?: throw Exception("No session")
            }

            _uiState.update {
                it.copy(isLoading = false, grades = result, semesterSelectorSelection = selectedSemester)
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
