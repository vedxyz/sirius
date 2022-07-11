package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import xyz.vedat.sirius.R
import xyz.vedat.sirius.viewmodels.LoginViewModel

class AutomaticVerificationFragment : Fragment(R.layout.fragment_verification_automatic) {
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authenticationResult.observe(viewLifecycleOwner) {
            if (it.success) {
                findNavController().navigate(R.id.action_automatic_verification_navfragment_to_srs_navactivity)
            } else {
                Log.e("AUTHENTICATION", "Failure Reason: ${it.failureReason}'")
            }
        }
    }
}
