package ru.jobni.jobni

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class FragmentIntro : Fragment() {

    companion object {
        fun newInstance() = FragmentIntro()
    }


    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private var adapter: ViewPagerAdapterIntro? = null

    var LOG_TAG = "my_log"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intro, container, false)
        viewPager = view.findViewById (R.id.pager)
        tabLayout = view.findViewById(R.id.tabDots)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tabLayout.setupWithViewPager(viewPager, true)

        adapter = ViewPagerAdapterIntro(activity!!.supportFragmentManager)
        viewPager.setAdapter(adapter)
    }
}
