package xyz.vedat.sirius.adapters

import androidx.fragment.app.Fragment
import srs.data.DailySchedule
import xyz.vedat.sirius.fragments.authenticated.components.DailyScheduleFragment

class WeeklySchedulePagerAdapter(fragment: Fragment) : RefreshablePagerAdapter<DailySchedule>(fragment) {
    override fun createFragment(position: Int): Fragment {
        return DailyScheduleFragment.newInstance(dataList!![position])
    }
}
