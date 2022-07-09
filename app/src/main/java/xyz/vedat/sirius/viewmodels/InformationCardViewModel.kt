package xyz.vedat.sirius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.data.InformationCard
import xyz.vedat.sirius.SessionManager

class InformationCardViewModel : ViewModel() {
    private val _informationCard: MutableLiveData<InformationCard> by lazy {
        MutableLiveData<InformationCard>().also { fetch() }
    }

    val informationCard: LiveData<InformationCard>
        get() = _informationCard

    private fun fetch() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SessionManager.session?.getInformationCard() ?: throw Exception("No session")
            }

            _informationCard.value = result
        }
    }
}
