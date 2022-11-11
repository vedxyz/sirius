package xyz.vedat.sirius.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import srs.data.AttendanceItem
import xyz.vedat.sirius.R

class AttendanceAdapter : ListAdapter<AttendanceItem, AttendanceAdapter.ViewHolder>(AttendanceDiffCallback) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateView = itemView.findViewById<TextView>(R.id.attendance_item_date_tw)
        private val titleView = itemView.findViewById<TextView>(R.id.attendance_item_title_tw)
        private val attendanceView = itemView.findViewById<TextView>(R.id.attendance_item_attendance_tw)

        fun bind(item: AttendanceItem) {
            dateView.text = item.date
            titleView.text = item.title
            attendanceView.text = item.attendance
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendance_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    private object AttendanceDiffCallback : DiffUtil.ItemCallback<AttendanceItem>() {
        override fun areItemsTheSame(oldItem: AttendanceItem, newItem: AttendanceItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AttendanceItem, newItem: AttendanceItem): Boolean {
            return oldItem.date == newItem.date && oldItem.title == newItem.title && oldItem.attendance == newItem.attendance
        }
    }
}
