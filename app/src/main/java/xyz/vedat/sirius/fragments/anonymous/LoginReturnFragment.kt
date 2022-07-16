package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import xyz.vedat.sirius.R
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.fragments.mainNavController

class LoginReturnFragment : Fragment(R.layout.fragment_login_return) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This lets us handle exiting the app regardless of the backstack
        requireActivity().let {
            it.onBackPressedDispatcher.addCallback(this) {
                Log.v(defaultLogTag, "Back pressed on return fragment, finishing activity")
                it.finish()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.login_return_button).setOnClickListener {
            Log.v(defaultLogTag, "Attempting to navigate back to SRS hub")
            mainNavController.navigate(R.id.action_anonymous_hub_to_srs_hub)
        }
    }
}
