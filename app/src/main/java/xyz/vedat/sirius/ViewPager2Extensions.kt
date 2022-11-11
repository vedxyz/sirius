package xyz.vedat.sirius

import android.view.View
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.disableViewWhileScrolling(view: View) =
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            view.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
        }
    })
