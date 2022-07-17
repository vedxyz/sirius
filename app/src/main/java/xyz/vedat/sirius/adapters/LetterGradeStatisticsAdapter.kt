package xyz.vedat.sirius.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.vedat.sirius.R
import xyz.vedat.sirius.defaultLogTag
import xyz.vedat.sirius.viewmodels.LetterGradeStatisticsViewModel

class LetterGradeStatisticsAdapter(private val onClick: (LetterGradeStatisticsViewModel.Item) -> Unit) :
    ListAdapter<LetterGradeStatisticsViewModel.Item, LetterGradeStatisticsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(itemView: View, val onClick: (LetterGradeStatisticsViewModel.Item) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val courseView = itemView.findViewById<TextView>(R.id.letter_grade_statistics_course_tw)
        private val nameView = itemView.findViewById<TextView>(R.id.letter_grade_statistics_name_tw)
        private val semesterView = itemView.findViewById<TextView>(R.id.letter_grade_statistics_semester_tw)
        private var currentItem: LetterGradeStatisticsViewModel.Item? = null

        init {
            // FIXME: The very first click on the LGS view is not registered?
            itemView.setOnClickListener { currentItem?.let { onClick(it) } }
        }

        fun bind(item: LetterGradeStatisticsViewModel.Item) {
            currentItem = item

            courseView.text = item.course.let { "${it.department} ${it.number}" }
            nameView.text = item.name
            semesterView.text = item.semester.let { "${it.year}-${it.year.toInt() + 1} ${it.season}" }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.letter_grade_statistics_item, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private object DiffCallback : DiffUtil.ItemCallback<LetterGradeStatisticsViewModel.Item>() {
        override fun areItemsTheSame(
            oldItem: LetterGradeStatisticsViewModel.Item,
            newItem: LetterGradeStatisticsViewModel.Item
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LetterGradeStatisticsViewModel.Item,
            newItem: LetterGradeStatisticsViewModel.Item
        ): Boolean {
            return oldItem.course == newItem.course && oldItem.semester == newItem.semester
        }
    }
}
