package xyz.vedat.sirius.adapters

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class RefreshablePagerAdapter<T>(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var idCounter = 0L
    private var itemIds: List<Long> = emptyList()

    override fun getItemId(position: Int): Long = itemIds[position]
    override fun containsItem(itemId: Long): Boolean = itemIds.contains(itemId)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<T>) {
        onSubmittingList(newList)
        itemIds = newList.indices.map { idCounter++ }
        notifyDataSetChanged()
    }

    protected abstract fun onSubmittingList(newList: List<T>)

    abstract override fun getItemCount(): Int
    abstract override fun createFragment(position: Int): Fragment
}
