package xyz.vedat.sirius.adapters

import androidx.fragment.app.Fragment
import srs.data.CourseGrades
import xyz.vedat.sirius.fragments.authenticated.components.CourseGradesFragment

class CourseGradesPagerAdapter(fragment: Fragment) : RefreshablePagerAdapter<CourseGrades>(fragment) {
    override fun createFragment(position: Int): Fragment {
        return CourseGradesFragment.newInstance(dataList!![position])
    }
}
