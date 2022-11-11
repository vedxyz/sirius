package xyz.vedat.sirius.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.data.WeeklySchedule
import xyz.vedat.sirius.SessionManager

class WeeklyScheduleViewModel : ViewModel() {
    data class UiState(val isLoading: Boolean = true, val weeklySchedule: WeeklySchedule? = null)

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun fetch() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getWeeklySchedule() ?: throw Exception("No session")
            }

            _uiState.update {
                it.copy(isLoading = false, weeklySchedule = result)
            }
        }
    }
}
