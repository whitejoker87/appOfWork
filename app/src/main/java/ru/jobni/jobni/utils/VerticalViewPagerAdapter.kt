package ru.jobni.jobni.utils

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jobni.jobni.fragments.FragmentRegViewPagerOne
import ru.jobni.jobni.fragments.FragmentRegViewPagerThree
import ru.jobni.jobni.fragments.FragmentRegViewPagerTwo
import ru.jobni.jobni.fragments.FragmentVerticalViewPager

class VerticalViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val tabTitles = arrayOf("Начало", "Середина", "Конец")

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> FragmentRegViewPagerOne()
            1 -> FragmentRegViewPagerTwo()
            2 -> FragmentRegViewPagerThree()
            else -> FragmentRegViewPagerOne()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}