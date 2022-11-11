package xyz.vedat.sirius.fragments.authenticated.components

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import srs.data.CourseAttendance
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.AttendanceAdapter
import xyz.vedat.sirius.getSerializableCompat

private const val ATTENDANCE_KEY = "ATTENDANCE_KEY"

class CourseAttendanceFragment : Fragment(R.layout.fragment_attendance_course_view) {
    private lateinit var attendanceRatioKVItem: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var attendance: CourseAttendance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attendance =
            arguments?.getSerializableCompat(ATTENDANCE_KEY) ?: throw IllegalStateException("Should not be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attendanceRatioKVItem = view.findViewById(R.id.attendance_ratio_kv_item)
        recyclerView = view.findViewById(R.id.attendance_course_recyclerview)

        attendanceRatioKVItem.let {
            it.findViewById<TextView>(R.id.key_value_item_key).text = getString(R.string.attendance_ratio_label)
            it.findViewById<TextView>(R.id.key_value_item_value).text = attendance.ratio
        }

        val adapter = AttendanceAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(attendance.data)
    }

    companion object {
        fun newInstance(attendance: CourseAttendance): CourseAttendanceFragment {
            val fragment = CourseAttendanceFragment()
            val bundle = Bundle()
            bundle.putSerializable(ATTENDANCE_KEY, attendance)
            fragment.arguments = bundle
            return fragment
        }
    }
}
