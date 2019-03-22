package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.jobni.jobni.R
import ru.jobni.jobni.utils.IntroViewPagerAdapter

class FragmentIntro : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private var adapter: IntroViewPagerAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro, container, false)
        viewPager = view.findViewById(R.id.pager)
        tabLayout = view.findViewById(R.id.tabDots)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tabLayout.setupWithViewPager(viewPager, true)

        adapter = IntroViewPagerAdapter(activity!!.supportFragmentManager)
        viewPager.adapter = adapter
    }
}
