package xyz.vedat.sirius.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.data.CourseAttendance
import srs.data.Semester
import xyz.vedat.sirius.SessionManager
import xyz.vedat.sirius.fragments.authenticated.components.ISemesterSelectorUiState
import xyz.vedat.sirius.fragments.authenticated.components.ISemesterSelectorViewModel

class AttendanceViewModel : ViewModel(), ISemesterSelectorViewModel {
    data class UiState(
        val isLoading: Boolean = true,
        val attendance: List<CourseAttendance>? = null,
        override val isSemesterSelectorLoading: Boolean = true,
        override val semesterSelectorOptions: List<Semester>? = null,
        override val semesterSelectorSelection: Semester? = null,
        val shouldResetPosition: Boolean = false
    ) : ISemesterSelectorUiState

    private val _uiState = MutableStateFlow(UiState())
    override val uiState = _uiState.asStateFlow()

    fun fetch(semester: Semester? = null) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getAttendance(semester) ?: throw Exception("No session")
            }
            val selectedSemester = semester ?: withContext(Dispatchers.IO) {
                SessionManager.session?.getCurrentSemester() ?: throw Exception("No session")
            }

            val shouldResetPosition = _uiState.value.semesterSelectorSelection?.let {
                it != selectedSemester
            } ?: false

            _uiState.update {
                it.copy(
                    isLoading = false,
                    attendance = result,
                    semesterSelectorSelection = selectedSemester,
                    shouldResetPosition = shouldResetPosition
                )
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

    fun resetPositionConsumed() = _uiState.update { it.copy(shouldResetPosition = false) }
}
