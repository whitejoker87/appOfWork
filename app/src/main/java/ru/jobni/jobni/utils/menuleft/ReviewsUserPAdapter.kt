package ru.jobni.jobni.utils.menuleft

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jobni.jobni.R
import ru.jobni.jobni.fragments.menuleft.FragmentReviewsUserAbout
import ru.jobni.jobni.fragments.menuleft.FragmentReviewsUserMy

class ReviewsUserPAdapter(
        private val fragment: FragmentManager,
        private val _context: Context
) : FragmentPagerAdapter(fragment) {

    private val mFragmentList: ArrayList<Fragment> = arrayListOf(FragmentReviewsUserAbout(), FragmentReviewsUserMy())

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> _context.getString(R.string.reviews_tab_about)
            else -> {
                return _context.getString(R.string.reviews_tab_my)
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