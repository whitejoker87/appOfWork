package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import ru.jobni.jobni.NavRVAdapter
import ru.jobni.jobni.R
import ru.jobni.jobni.RepositoryUser.makeNavigationListUser
import ru.jobni.jobni.databinding.FragmentNavLeftUserBinding
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentNavLeftUser : Fragment() {

    private lateinit var navRecyclerView: RecyclerView
    private lateinit var navAdapter: NavRVAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentNavLeftUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nav_left_user, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        navRecyclerView = binding.rvNavLeftUser

        buildCardsRecyclerView()

        return view
    }

    private fun buildCardsRecyclerView() {

        navAdapter = NavRVAdapter(makeNavigationListUser(), activity!!)
        navAdapter.setHasStableIds(true)
        navRecyclerView.adapter = navAdapter

        navAdapter.setOnGroupClickListener(object : OnGroupClickListener {
            override fun onGroupClick(flatPos: Int): Boolean {
                if (flatPos == 0) {
                    viewModel.setFragmentLaunch("Summary")
                    viewModel.setOpenDrawerLeft(false)
                    return false
                }

                if (flatPos == 1) {
                    viewModel.setFragmentLaunch("ReviewsUser")
                    viewModel.setOpenDrawerLeft(false)
                    return false
                }

                if (flatPos == 2) {
                    viewModel.setFragmentLaunch("ProfileUser")
                    viewModel.setOpenDrawerLeft(false)
                    return false
                }

                return false
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navAdapter.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navAdapter.onRestoreInstanceState(savedInstanceState)
    }
}
