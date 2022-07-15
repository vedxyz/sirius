package xyz.vedat.sirius.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import xyz.vedat.sirius.R

val Fragment.mainNavController: NavController
    get() = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment)
