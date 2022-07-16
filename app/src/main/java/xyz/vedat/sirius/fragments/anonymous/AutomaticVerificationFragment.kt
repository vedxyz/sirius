package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.fragments.mainNavController
import xyz.vedat.sirius.viewmodels.LoginViewModel

class AutomaticVerificationFragment : Fragment(R.layout.loading_screen) {
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.v(defaultLogTag, "Preventing back press during verification")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.loading_screen_textview).text =
            getString(R.string.login_automatic_verification_text_attempting)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoggedIn) {
                        Log.v(defaultLogTag, "Verification complete, navigating to SRS hub")
                        findNavController().popBackStack()
                        mainNavController.navigate(R.id.srs_hub_navfragment)
                    } else if (it.errorMessage != null) {
                        Log.e("AUTHENTICATION", "Failure Reason: '${it.errorMessage}'")
                    }
                }
            }
        }
    }
}
