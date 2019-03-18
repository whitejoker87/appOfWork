package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentReviewsOwnerBinding
import ru.jobni.jobni.utils.ReviewsOwnerPAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentReviewsOwner : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentReviewsOwnerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews_owner, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        val fragmentAdapter = ReviewsOwnerPAdapter(activity!!.supportFragmentManager, context!!)
        binding.viewPagerReviewsOwner.adapter = fragmentAdapter
        binding.tabLayoutReviewsOwner.setupWithViewPager(binding.viewPagerReviewsOwner)

        binding.fabReviewsOwner.setOnClickListener { fabView ->
            Snackbar.make(fabView, "Reviews Owner FAB", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.setBottomNavigationViewVisible(false)
    }
}


