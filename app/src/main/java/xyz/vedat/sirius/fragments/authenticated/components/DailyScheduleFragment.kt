package xyz.vedat.sirius.fragments.authenticated.components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import srs.data.DailySchedule
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.DailyScheduleAdapter
import xyz.vedat.sirius.getSerializableCompat

const val DAILY_SCHEDULE_KEY = "DAILY_SCHEDULE_KEY"

class DailyScheduleFragment : Fragment(R.layout.fragment_daily_schedule) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dailySchedule: DailySchedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dailySchedule =
            arguments?.getSerializableCompat(DAILY_SCHEDULE_KEY) ?: throw IllegalStateException("Should not be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.daily_schedule_recyclerview)

        val adapter = DailyScheduleAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(dailySchedule.timeSlots.filterNot { it.details.isNullOrEmpty() })
    }

    companion object {
        fun newInstance(dailySchedule: DailySchedule): DailyScheduleFragment {
            val fragment = DailyScheduleFragment()
            val bundle = Bundle()
            bundle.putSerializable(DAILY_SCHEDULE_KEY, dailySchedule)
            fragment.arguments = bundle
            return fragment
        }
    }
}
