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

class SemesterCoursesViewModel : ViewModel() {
    data class UiState(val isLoading: Boolean = true, val semesterCourses: SemesterCourses? = null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

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
}
