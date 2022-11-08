package xyz.vedat.sirius.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import srs.data.CourseGrades
import srs.data.GradeItem
import xyz.vedat.sirius.R

const val VIEW_TYPE_GRADE_ITEM = 1
const val VIEW_TYPE_GRADE_CATEGORY = 2

class CourseGradesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: List<AdapterViewItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(grades: CourseGrades) {
        data = grades.categories.flatMap { category ->
            arrayOf(
                GradeCategoryViewItem(category.type), *category.items.map { GradeViewItem(it) }.toTypedArray()
            ).asIterable()
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is GradeViewItem -> VIEW_TYPE_GRADE_ITEM
            is GradeCategoryViewItem -> VIEW_TYPE_GRADE_CATEGORY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        fun inflateWith(@LayoutRes layout: Int): View =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return when (viewType) {
            VIEW_TYPE_GRADE_ITEM -> GradeItemViewHolder(inflateWith(R.layout.grade_item))
            VIEW_TYPE_GRADE_CATEGORY -> GradeCategoryViewHolder(inflateWith(R.layout.grade_category))
            else -> throw Exception("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GradeItemViewHolder -> holder.bind(data[position] as GradeViewItem)
            is GradeCategoryViewHolder -> holder.bind(data[position] as GradeCategoryViewItem)
        }
    }

    override fun getItemCount(): Int = data.size

    sealed class AdapterViewItem
    class GradeViewItem(val grade: GradeItem) : AdapterViewItem()
    class GradeCategoryViewItem(val type: String) : AdapterViewItem()

    abstract class AdapterViewHolder<T : AdapterViewItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    class GradeItemViewHolder(itemView: View) : AdapterViewHolder<GradeViewItem>(itemView) {
        private val dateView = itemView.findViewById<TextView>(R.id.grade_item_date_tw)
        private val titleView = itemView.findViewById<TextView>(R.id.grade_item_title_tw)
        private val gradeView = itemView.findViewById<TextView>(R.id.grade_item_grade_tw)
        private val commentView = itemView.findViewById<TextView>(R.id.grade_item_comment_tw)

        override fun bind(item: GradeViewItem) {
            dateView.text = item.grade.date
            titleView.text = item.grade.title
            gradeView.text = item.grade.grade
            commentView.text = item.grade.comment

            if (item.grade.comment.isBlank()) {
                commentView.visibility = View.GONE
            }
        }
    }

    class GradeCategoryViewHolder(itemView: View) : AdapterViewHolder<GradeCategoryViewItem>(itemView) {
        private val categoryView = itemView.findViewById<TextView>(R.id.grade_category_tw)

        override fun bind(item: GradeCategoryViewItem) {
            categoryView.text = item.type
        }
    }
}
