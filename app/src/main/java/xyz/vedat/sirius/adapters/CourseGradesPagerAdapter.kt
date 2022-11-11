package xyz.vedat.sirius.adapters

import androidx.fragment.app.Fragment
import srs.data.CourseGrades
import xyz.vedat.sirius.fragments.authenticated.components.CourseGradesFragment

class CourseGradesPagerAdapter(fragment: Fragment) : RefreshablePagerAdapter<CourseGrades>(fragment) {
    private var grades: List<CourseGrades>? = null

    override fun onSubmittingList(newList: List<CourseGrades>) {
        grades = newList
    }

    override fun getItemCount() = grades?.size ?: 0

    override fun createFragment(position: Int): Fragment {
        return CourseGradesFragment.newInstance(grades!![position])
    }
}
