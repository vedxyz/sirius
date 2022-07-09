package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputLayout
import xyz.vedat.sirius.R
import xyz.vedat.sirius.SessionManager
import xyz.vedat.sirius.viewmodels.LoginViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var automaticVerificationItems: List<View>
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        if (SessionManager.hasSession) {
            findNavController().navigate(R.id.action_login_bnav_item_to_login_return_navfragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        automaticVerificationItems = listOf(
            view.findViewById(R.id.login_email_address),
            view.findViewById(R.id.login_email_password)
        )

        watchEmailText(view)
        toggleVerificationMethod(view)
        registerContinueButtonListener(view)
    }

    private fun watchEmailText(view: View) {
        val emailTextLayout = view.findViewById<TextInputLayout>(R.id.login_email_address)
        emailTextLayout.editText?.addTextChangedListener(object : TextWatcher {
            var hadFullEmail = false

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val containsFullEmail = text?.contains('@') == true
                if (!hadFullEmail && containsFullEmail)
                    emailTextLayout.suffixText = ""
                else if (hadFullEmail && !containsFullEmail)
                    emailTextLayout.suffixText = getString(R.string.login_bilkent_email_suffix)
                hadFullEmail = containsFullEmail
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun toggleVerificationMethod(view: View) {
        view.findViewById<MaterialButtonToggleGroup>(R.id.verification_selector)
            .addOnButtonCheckedListener { _, checkedId, isChecked ->
                when (checkedId) {
                    R.id.verification_selector_manual -> {}
                    R.id.verification_selector_automatic -> {
                        automaticVerificationItems.forEach {
                            it.visibility = if (isChecked) View.VISIBLE else View.GONE
                        }
                    }
                }
            }
    }

    private fun registerContinueButtonListener(view: View) {
        val toggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.verification_selector)
        val bilkentId = view.findViewById<TextInputLayout>(R.id.login_username)
        val password = view.findViewById<TextInputLayout>(R.id.login_password)
        val email = view.findViewById<TextInputLayout>(R.id.login_email_address)
        val emailPassword = view.findViewById<TextInputLayout>(R.id.login_email_password)

        view.findViewById<Button>(R.id.login_submit).setOnClickListener {
            val bilkentIdValue = bilkentId.editText!!.text.toString()
            val passwordValue = password.editText!!.text.toString()
            val emailValue = resolveEmail(email.editText!!.text.toString())
            val emailPasswordValue = emailPassword.editText!!.text.toString()

            when (toggleGroup.checkedButtonId) {
                R.id.verification_selector_manual -> {
                    viewModel.beginManualVerification(view, bilkentIdValue, passwordValue)
                }
                R.id.verification_selector_automatic -> {
                    viewModel.beginAutomaticVerification(
                        view,
                        bilkentIdValue,
                        passwordValue,
                        emailValue,
                        emailPasswordValue
                    )
                }
            }
        }
    }

    private fun resolveEmail(emailString: String) =
        if (emailString.contains('@')) emailString else emailString + getString(R.string.login_bilkent_email_suffix)
}
