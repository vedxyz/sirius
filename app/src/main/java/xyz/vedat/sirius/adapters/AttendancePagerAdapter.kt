package xyz.vedat.sirius.adapters

import androidx.fragment.app.Fragment
import srs.data.CourseAttendance
import xyz.vedat.sirius.fragments.authenticated.components.CourseAttendanceFragment

class AttendancePagerAdapter(fragment: Fragment) : RefreshablePagerAdapter<CourseAttendance>(fragment) {
    override fun createFragment(position: Int): Fragment {
        return CourseAttendanceFragment.newInstance(dataList!![position])
    }
}
