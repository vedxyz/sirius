package xyz.vedat.sirius.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import anonymous.PublicBilkentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AcademicCalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AcademicCalendarUiState())
    val uiState = _uiState.asStateFlow()

    fun fetch() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                PublicBilkentProvider.getAcademicCalendar()
            }

            _uiState.update {
                it.copy(isLoading = false, academicCalendar = result)
            }
        }
    }
}
