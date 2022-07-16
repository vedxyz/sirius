package xyz.vedat.sirius.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import anonymous.data.AcademicCalendarEventType
import anonymous.data.AcademicCalendarItem
import xyz.vedat.sirius.R

class AcademicCalendarAdapter :
    ListAdapter<AcademicCalendarItem, AcademicCalendarAdapter.ViewHolder>(AcademicCalendarDiffCallback) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val keyView = itemView.findViewById<TextView>(R.id.key_value_item_key)
        private val valueView = itemView.findViewById<TextView>(R.id.key_value_item_value)

        fun bind(item: AcademicCalendarItem) {
            keyView.text = item.date
            valueView.text = item.event

            itemView.setBackgroundColor(
                when (item.type) {
                    AcademicCalendarEventType.EnglishPrep -> Color.GREEN
                    AcademicCalendarEventType.StudentAffairs -> Color.BLUE
                    AcademicCalendarEventType.Vacation -> Color.RED
                    else -> Color.TRANSPARENT
                }
            )
            itemView.background.alpha = if (item.type == null) 0 else 50
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.key_value_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private object AcademicCalendarDiffCallback : DiffUtil.ItemCallback<AcademicCalendarItem>() {
        override fun areItemsTheSame(oldItem: AcademicCalendarItem, newItem: AcademicCalendarItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AcademicCalendarItem, newItem: AcademicCalendarItem): Boolean {
            return oldItem.date == newItem.date && oldItem.event == newItem.event && oldItem.type == newItem.type
        }
    }
}
