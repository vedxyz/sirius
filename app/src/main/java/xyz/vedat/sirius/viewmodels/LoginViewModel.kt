package xyz.vedat.sirius.viewmodels

import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import srs.ManualVerificationIntermediary
import srs.SRSSession
import xyz.vedat.sirius.R
import xyz.vedat.sirius.SessionManager

class LoginViewModel : ViewModel() {
    var manualVerificationIntermediary: ManualVerificationIntermediary? = null
        private set

    fun beginManualVerification(view: View, bilkentId: String, password: String) {
        val navController = view.findNavController()
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SRSSession.withManualVerification(bilkentId, password)
            }

            manualVerificationIntermediary = result
            navController.navigate(R.id.action_login_bnav_item_to_manual_verification_navfragment)
        }
    }

    fun completeManualVerification(view: View, verificationCode: String) {
        val navController = view.findNavController()
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                manualVerificationIntermediary?.let { it.verify(verificationCode) }
            }

            if (result == null) {
                Log.w("Authentication", "Couldn't obtain SRS session")
                return@launch
            }

            completeVerification(
                navController,
                R.id.action_manual_verification_navfragment_to_srs_navactivity,
                result
            )
        }
    }

    fun beginAutomaticVerification(
        view: View,
        bilkentId: String,
        password: String,
        email: String,
        emailPassword: String
    ) {
        val navController = view.findNavController()
        navController.navigate(R.id.action_login_bnav_item_to_automatic_verification_navfragment)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                SRSSession.withAutomatedVerification(bilkentId, password, email, emailPassword)
            }

            completeVerification(
                navController,
                R.id.action_automatic_verification_navfragment_to_srs_navactivity,
                result
            )
        }
    }

    private fun completeVerification(
        navController: NavController,
        @IdRes destinationAction: Int,
        session: SRSSession
    ) {
        SessionManager.session = session
        navController.navigate(destinationAction)
    }
}
