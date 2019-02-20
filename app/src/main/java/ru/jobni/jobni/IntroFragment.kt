package ru.jobni.jobni

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.jobni.jobni.databinding.IntroFragmentBinding
import ru.jobni.jobni.model.IntroSlide

import ru.jobni.jobni.viewmodel.IntroViewModel

class IntroFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFragment()
    }


    private lateinit var mImageViewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private var adapter: IntroViewPagerAdapter? = null

    var LOG_TAG = "my_log"

    private lateinit var viewModel: IntroViewModel
    private lateinit var binding: IntroFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.intro_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(IntroViewModel::class.java)

        mImageViewPager = binding.pager
        tabLayout = binding.tabDots

        tabLayout.setupWithViewPager(mImageViewPager, true)

        adapter = IntroViewPagerAdapter(activity, viewModel.listSlides.value, object : IOnclickBtnSlide {
            override fun onClickButtonCallback(position: Int) {
                nextSlide()
            }
        })
        mImageViewPager.setAdapter(adapter)

//        viewModel.getListSlides().observe(this, Observer<List<IntroSlide>>() {
//            fun onChanged(@Nullable trainingSlides: List<IntroSlide>?) {
//
//                if (trainingSlides != null && trainingSlides.size > 0) {
//                    adapter!!.notifyDataSetChanged()
//                }
//            }
//        })
        viewModel.listSlides.observe(this, Observer<List<IntroSlide>> { introSlides -> adapter!!.notifyDataSetChanged()})
    }




    fun nextSlide() {
        Log.d(
            LOG_TAG,
            "" + mImageViewPager.getCurrentItem() + "  " + viewModel.listSlides.value?.size
        )
        if (mImageViewPager.getCurrentItem() < viewModel.listSlides.value!!.size.minus(1)) {
            mImageViewPager.setCurrentItem(mImageViewPager.getCurrentItem() + 1, true)
        } //else {
//            viewModel.saveLaunchFlag()
//            viewModel.setFragmentOrActivityLaunch("login")
//        }
    }

}
