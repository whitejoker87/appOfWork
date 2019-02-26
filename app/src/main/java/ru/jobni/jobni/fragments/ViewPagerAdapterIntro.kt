package ru.jobni.jobni.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jobni.jobni.R


class ViewPagerAdapterIntro(fm: FragmentManager) : FragmentPagerAdapter(fm) {

//    private lateinit var onclickBtnSlide: IOnclickBtnSlide
//
//    init {
//        this.onclickBtnSlide = onclickBtnSlide
//    }


    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return FragmentIntroSlide.newInstance(R.layout.c_intro_01)
            1 -> return FragmentIntroSlide.newInstance(R.layout.c_intro_02)
            2 -> return FragmentIntroSlide.newInstance(R.layout.c_intro_03)
            3 -> return FragmentIntroSlide.newInstance(R.layout.c_intro_04)
            4 -> return FragmentIntroSlide.newInstance(R.layout.c_intro_05)
        }
        return FragmentIntroSlide.newInstance(R.layout.c_intro_01)
    }

    override fun getCount(): Int= 5

    override fun getPageTitle(position: Int): CharSequence? = null

}