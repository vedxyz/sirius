package xyz.vedat.sirius.fragments.authenticated.components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import srs.data.CourseGrades
import xyz.vedat.sirius.R
import xyz.vedat.sirius.adapters.CourseGradesAdapter
import xyz.vedat.sirius.getSerializableCompat

private const val GRADES_KEY = "GRADES_KEY"

class CourseGradesFragment : Fragment(R.layout.fragment_grade_course_view) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var grades: CourseGrades

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        grades = arguments?.getSerializableCompat(GRADES_KEY) ?: throw IllegalStateException("Should not be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.grades_course_recyclerview)

        val adapter = CourseGradesAdapter()
        recyclerView.adapter = adapter
        adapter.submitData(grades)
    }

    companion object {
        fun newInstance(grades: CourseGrades): CourseGradesFragment {
            val fragment = CourseGradesFragment()
            val bundle = Bundle()
            bundle.putSerializable(GRADES_KEY, grades)
            fragment.arguments = bundle
            return fragment
        }
    }
}
