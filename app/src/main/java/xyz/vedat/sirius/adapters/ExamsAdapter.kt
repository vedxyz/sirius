package xyz.vedat.sirius.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import srs.data.Exam
import xyz.vedat.sirius.R
import java.time.format.DateTimeFormatter

class ExamsAdapter : ListAdapter<Exam, ExamsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseView = itemView.findViewById<TextView>(R.id.exam_course_tw)
        private val typeView = itemView.findViewById<TextView>(R.id.exam_type_tw)
        private val classroomsView = itemView.findViewById<TextView>(R.id.exam_classrooms_tw)
        private val timeView = itemView.findViewById<TextView>(R.id.exam_time_tw)

        private val timeFormatter = DateTimeFormatter.ofPattern("MMM d', 'HH':'mm")

        fun bind(item: Exam) {
            courseView.text = item.courseName
            typeView.text = item.examType
            classroomsView.text = item.classrooms?.joinToString() ?: "N/A"
            timeView.text = item.startingTime.format(timeFormatter) + " (${item.timeBlock})"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private object DiffCallback : DiffUtil.ItemCallback<Exam>() {
        override fun areItemsTheSame(oldItem: Exam, newItem: Exam): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Exam, newItem: Exam): Boolean {
            return oldItem.classrooms == newItem.classrooms
                && oldItem.examType == newItem.examType
                && oldItem.startingTime == newItem.startingTime
                && oldItem.timeBlock == newItem.timeBlock
        }
    }
}
