package ru.jobni.jobni.fragments.menuleft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentCompanyVacancyBinding
import ru.jobni.jobni.utils.menuleft.VacancyPAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentCompanyVacancy : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentCompanyVacancyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company_vacancy, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        val fragmentAdapter = VacancyPAdapter(activity!!.supportFragmentManager, context!!)
        binding.viewPagerVacancy.adapter = fragmentAdapter
        binding.tabLayoutVacancy.setupWithViewPager(binding.viewPagerVacancy)

        binding.fabVacancy.setOnClickListener { fabView ->
            Snackbar.make(fabView, "CompanyVacancy FAB", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.setBottomNavigationViewVisible(false)
    }
}


