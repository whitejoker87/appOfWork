package ru.jobni.jobni.utils.menuleft

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jobni.jobni.R
import ru.jobni.jobni.fragments.menuleft.FragmentNavLeftOwnerAuthOff
import ru.jobni.jobni.fragments.menuleft.FragmentNavLeftUserAuthOff

class NavPALeftAuthOff(fragment: FragmentManager, private val _context: Context) : FragmentPagerAdapter(fragment) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentNavLeftUserAuthOff()
            else -> {
                return FragmentNavLeftOwnerAuthOff()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> _context.getString(R.string.nav_header_left_user_tab)
            else -> {
                return _context.getString(R.string.nav_header_left_owner_tab)
            }
        }
    }
}