package xyz.vedat.sirius.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import srs.data.DailyScheduleItem
import xyz.vedat.sirius.R

class DailyScheduleAdapter :
    ListAdapter<DailyScheduleItem, DailyScheduleAdapter.ViewHolder>(DailyScheduleDiffCallback) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeslotView = itemView.findViewById<TextView>(R.id.daily_schedule_item_timeslot_tw)
        private val detailsView = itemView.findViewById<TextView>(R.id.daily_schedule_item_details_tw)

        fun bind(item: DailyScheduleItem) {
            timeslotView.text = item.timeSlot.representation
            detailsView.text = item.details!!.joinToString(System.lineSeparator())

            if (item.details!!.contains("(Spare Hour)"))
                itemView.alpha = 0.6f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_schedule_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private object DailyScheduleDiffCallback : DiffUtil.ItemCallback<DailyScheduleItem>() {
        override fun areItemsTheSame(oldItem: DailyScheduleItem, newItem: DailyScheduleItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DailyScheduleItem, newItem: DailyScheduleItem): Boolean {
            return oldItem.timeSlot == newItem.timeSlot && oldItem.details == newItem.details
        }
    }
}
