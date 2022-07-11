package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import xyz.vedat.sirius.R
import xyz.vedat.sirius.viewmodels.LoginViewModel

class ManualVerificationFragment : Fragment(R.layout.fragment_verification_manual) {
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthentication(view)
        registerSubmitButtonListener(view)
    }

    private fun observeAuthentication(view: View) {
        viewModel.authenticationResult.observe(viewLifecycleOwner) {
            formatReferenceText(view, it.manualVerificationIntermediary?.reference)

            if (it.success && it.session != null) {
                findNavController().navigate(R.id.action_manual_verification_navfragment_to_srs_navactivity)
            } else if (!it.success) {
                Log.e("AUTHENTICATION", "Failure Reason: '${it.failureReason}'")
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
