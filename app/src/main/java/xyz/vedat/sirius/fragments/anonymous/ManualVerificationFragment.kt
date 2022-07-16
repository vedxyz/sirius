package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.fragments.mainNavController
import xyz.vedat.sirius.viewmodels.LoginViewModel

class ManualVerificationFragment : Fragment(R.layout.fragment_verification_manual) {
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.v(defaultLogTag, "Preventing back press during verification")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleUiState(view)
        registerSubmitButtonListener(view)
    }

    private fun handleUiState(view: View) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    formatReferenceText(view, it.manualVerificationReference)

                    if (it.isLoggedIn) {
                        Log.v(defaultLogTag, "Verification complete, navigating to SRS hub")
                        findNavController().popBackStack()
                        mainNavController.navigate(R.id.action_anonymous_hub_to_srs_hub)
                    } else if (it.errorMessage != null) {
                        Log.e("AUTHENTICATION", "Failure Reason: '${it.errorMessage}'")
                    }
                }
            }
        }
    }

    private fun formatReferenceText(view: View, value: String?) {
        view.findViewById<TextView>(R.id.login_manual_verification_reference_tw).text = getString(
            R.string.login_manual_verification_reference_text,
            value ?: "????"
        )
    }

    private fun registerSubmitButtonListener(view: View) {
        val verificationInput = view.findViewById<TextInputLayout>(R.id.login_manual_verification_input)

        view.findViewById<Button>(R.id.login_manual_verification_submit).setOnClickListener {
            viewModel.completeManualVerification(verificationInput.editText!!.text.toString())
        }
    }
}
