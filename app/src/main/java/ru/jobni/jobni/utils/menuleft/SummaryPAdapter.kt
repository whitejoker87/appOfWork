package ru.jobni.jobni.utils.menuleft

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jobni.jobni.R
import ru.jobni.jobni.fragments.menuleft.FragmentSummaryUserActive
import ru.jobni.jobni.fragments.menuleft.FragmentSummaryUserArchive

class SummaryPAdapter(
        private val fragment: FragmentManager,
        private val _context: Context
) : FragmentPagerAdapter(fragment) {

    private val mFragmentList: ArrayList<Fragment> = arrayListOf(FragmentSummaryUserActive(), FragmentSummaryUserArchive())

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> _context.getString(R.string.summary_tab_active)
            else -> {
                return _context.getString(R.string.summary_tab_archive)
            }
        }
    }

    fun clear() {
        val transaction = fragment.beginTransaction()
        for (fragment in mFragmentList) {
            transaction.remove(fragment)
        }
        mFragmentList.clear()
        transaction.commitAllowingStateLoss()
    }
}