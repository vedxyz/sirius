package xyz.vedat.sirius.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import srs.data.SemesterCourseItem
import xyz.vedat.sirius.R

class SemesterCoursesAdapter : ListAdapter<SemesterCourseItem, SemesterCoursesAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val codeView = itemView.findViewById<TextView>(R.id.semester_course_code_tw)
        private val nameView = itemView.findViewById<TextView>(R.id.semester_course_name_tw)
        private val instructorView = itemView.findViewById<TextView>(R.id.semester_course_instructor_tw)
        private val creditsView = itemView.findViewById<View>(R.id.semester_course_credits_kv_item)
        private val typeView = itemView.findViewById<View>(R.id.semester_course_type_kv_item)

        init {
            creditsView.findViewById<TextView>(R.id.key_value_item_key).text = "Credits"
            typeView.findViewById<TextView>(R.id.key_value_item_key).text = "Type"
        }

        fun bind(item: SemesterCourseItem) {
            codeView.text = item.course.let { "${it.department} ${it.number}-${it.section}" }
            nameView.text = item.name
            instructorView.text = itemView.context.getString(R.string.semester_course_instructor_text, item.instructor)
            creditsView.findViewById<TextView>(R.id.key_value_item_value).text = item.credits
            typeView.findViewById<TextView>(R.id.key_value_item_value).text = item.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.semester_course_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private object DiffCallback : DiffUtil.ItemCallback<SemesterCourseItem>() {
        override fun areItemsTheSame(oldItem: SemesterCourseItem, newItem: SemesterCourseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SemesterCourseItem, newItem: SemesterCourseItem): Boolean {
            return oldItem.course == newItem.course
                && oldItem.name == newItem.name
                && oldItem.instructor == newItem.instructor
        }
    }
}
