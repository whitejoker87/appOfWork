package ru.jobni.jobni.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jobni.jobni.R
import ru.jobni.jobni.fragments.FragmentReviewsOwnerAbout
import ru.jobni.jobni.fragments.FragmentReviewsOwnerMy

class ReviewsOwnerPAdapter(fragment: FragmentManager, private val _context: Context) : FragmentPagerAdapter(fragment) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentReviewsOwnerAbout()
            else -> {
                return FragmentReviewsOwnerMy()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> _context.getString(R.string.reviews_tab_about)
            else -> {
                return _context.getString(R.string.reviews_tab_my)
            }
        }
    }
}