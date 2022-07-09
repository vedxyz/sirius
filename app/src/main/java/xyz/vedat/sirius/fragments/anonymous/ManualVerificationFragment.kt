package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import xyz.vedat.sirius.R
import xyz.vedat.sirius.viewmodels.LoginViewModel

class ManualVerificationFragment : Fragment(R.layout.fragment_verification_manual) {
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formatReferenceText(view)
        registerSubmitButtonListener(view)
    }

    private fun formatReferenceText(view: View) {
        view.findViewById<TextView>(R.id.login_manual_verification_reference_tw).text = getString(
            R.string.login_manual_verification_reference_text,
            viewModel.manualVerificationIntermediary?.reference ?: "????"
        )
    }

    private fun registerSubmitButtonListener(view: View) {
        val verificationInput =
            view.findViewById<TextInputLayout>(R.id.login_manual_verification_input)

        view.findViewById<Button>(R.id.login_manual_verification_submit).setOnClickListener {
            viewModel.completeManualVerification(view, verificationInput.editText!!.text.toString())
        }
    }
}
