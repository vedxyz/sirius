package xyz.vedat.sirius.fragments.authenticated

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import xyz.vedat.sirius.R
import xyz.vedat.sirius.viewmodels.InformationCardViewModel

class InformationCardFragment : Fragment(R.layout.fragment_information_card) {
    private val viewModel: InformationCardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.informationCard.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.srs_information_card_random_text).text = it.student.fullName
        }
    }
}
