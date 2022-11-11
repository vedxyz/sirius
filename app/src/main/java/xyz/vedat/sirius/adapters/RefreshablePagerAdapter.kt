package xyz.vedat.sirius.adapters

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class RefreshablePagerAdapter<T>(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var idCounter = 0L
    private var itemIds: List<Long> = emptyList()

    protected var dataList: List<T>? = null

    override fun getItemId(position: Int): Long = itemIds[position]
    override fun containsItem(itemId: Long): Boolean = itemIds.contains(itemId)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<T>) {
        val finalList = onSubmittingList(newList)
        dataList = finalList
        itemIds = finalList.indices.map { idCounter++ }
        notifyDataSetChanged()
    }

    protected open fun onSubmittingList(newList: List<T>) = newList

    override fun getItemCount(): Int = dataList?.size ?: 0

    abstract override fun createFragment(position: Int): Fragment
}
