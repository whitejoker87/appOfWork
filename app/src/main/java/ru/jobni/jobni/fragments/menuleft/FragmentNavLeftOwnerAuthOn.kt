package ru.jobni.jobni.fragments.menuleft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentNavLeftOwnerBinding
import ru.jobni.jobni.model.menu.left.RepositoryOwner.makeNavigationListOwnerAuthOn
import ru.jobni.jobni.utils.NavRVAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentNavLeftOwnerAuthOn : Fragment() {

    private lateinit var navRecyclerView: RecyclerView
    private lateinit var navAdapter: NavRVAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentNavLeftOwnerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nav_left_owner, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        navRecyclerView = binding.rvNavLeftOwner

        viewModel.getModelCompany().observe(this, Observer { company ->
            company?.let {
                buildRecyclerView()
            }
        })

        return view
    }

    private fun buildRecyclerView() {

        navAdapter = NavRVAdapter(makeNavigationListOwnerAuthOn(), activity!!)
        navRecyclerView.adapter = navAdapter

        navAdapter.setOnGroupClickListener(object : OnGroupClickListener {
            override fun onGroupClick(flatPos: Int): Boolean {
                if (flatPos == 0) {
                    viewModel.setFragmentLaunch("CompanyAddAuthOn")
                    viewModel.setOpenDrawerLeft(false)
                    return false
                }

                if (flatPos == 1) {
                    // Первый элемент в списке занят, поэтому это нужно учитывать flatPos -1
//                    viewModel.loadLeftMenuOwnerDataBalance(flatPos -1)
                    viewModel.loadLeftMenuOwnerCompanyVacancy(flatPos -1)
                    return false
                }

                return false
            }
        })
    }
}
