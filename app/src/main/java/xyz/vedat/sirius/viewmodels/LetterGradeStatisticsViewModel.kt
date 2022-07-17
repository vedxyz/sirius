package xyz.vedat.sirius.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.data.Course
import srs.data.Semester
import xyz.vedat.sirius.SessionManager

class LetterGradeStatisticsViewModel : ViewModel() {
    data class Item(
        val semester: Semester,
        val course: Course,
        val name: String,
        val fetch: () -> Unit,
        val image: Bitmap? = null
    )

    data class UiState(
        val isLoading: Boolean = true,
        val items: List<Item>? = null,
        val shouldNavigate: Boolean = false,
        val activeIndex: Int? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun fetch() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getCurriculum() ?: throw Exception("No session")
            }

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    items = result.semesters.flatMap { it.items }
                        .filterNot { it.semester == null }.reversed().mapIndexed { i, it ->
                            val (course, name) = if (it.course != null) Pair(it.course!!, it.name)
                            else Pair(it.replacement!!.course, it.replacement!!.name)
                            Item(it.semester!!, course, name, { fetch(i, it.semester!!, course) })
                        }
                )
            }
        }
    }

    private fun fetch(index: Int, semester: Semester, course: Course) {
        _uiState.update {
            it.copy(shouldNavigate = true)
        }

        if (uiState.value.items!![index].image != null) {
            _uiState.update {
                it.copy(activeIndex = index)
            }
        } else {
            viewModelScope.launch {
                val result = withContext(Dispatchers.IO) {
                    SessionManager.session?.getLetterGradeStatistics(semester, course) ?: throw Exception("No session")
                }

                _uiState.update { state ->
                    state.copy(items = state.items?.map { item ->
                        if (item.semester != semester || item.course != course) item else item.copy(
                            image = result.let { Base64.decode(it.substringAfter(','), Base64.DEFAULT) }
                                .let { BitmapFactory.decodeByteArray(it, 0, it.size) })
                    }, activeIndex = index)
                }
            }
        }
    }

    fun navigationConsumed() {
        _uiState.update {
            it.copy(shouldNavigate = false)
        }
    }

    fun dismissActive() {
        _uiState.update { state ->
            state.copy(activeIndex = null)
        }
    }
}
