package xyz.vedat.sirius.adapters

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import srs.data.CourseGrades
import xyz.vedat.sirius.fragments.authenticated.components.CourseGradesFragment

class CourseGradesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var idCounter = 0L
    private var itemIds: List<Long> = emptyList()

    private var grades: List<CourseGrades>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newGrades: List<CourseGrades>) {
        grades = newGrades
        itemIds = newGrades.indices.map { idCounter++ }
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = itemIds[position]
    override fun containsItem(itemId: Long): Boolean = itemIds.contains(itemId)

    override fun getItemCount() = grades?.size ?: 0

    override fun createFragment(position: Int): Fragment {
        return CourseGradesFragment.newInstance(grades!![position])
    }
}
