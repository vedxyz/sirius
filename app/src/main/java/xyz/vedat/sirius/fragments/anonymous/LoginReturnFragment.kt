package xyz.vedat.sirius.fragments.anonymous

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import xyz.vedat.sirius.R

class LoginReturnFragment : Fragment(R.layout.fragment_login_return) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.login_return_button).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_login_return_navfragment_to_srs_navactivity)
        }
    }
}
