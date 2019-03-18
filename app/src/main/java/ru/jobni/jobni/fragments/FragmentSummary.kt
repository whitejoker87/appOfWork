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
import ru.jobni.jobni.databinding.FragmentSummaryBinding
import ru.jobni.jobni.utils.SummaryPAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentSummary : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentSummaryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        val fragmentAdapter = SummaryPAdapter(activity!!.supportFragmentManager, context!!)
        binding.viewPagerSummary.adapter = fragmentAdapter
        binding.tabLayoutSummary.setupWithViewPager(binding.viewPagerSummary)

        binding.fabSummary.setOnClickListener { fabView ->
            Snackbar.make(fabView, "Summary FAB", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.setBottomNavigationViewVisible(false)
    }
}


