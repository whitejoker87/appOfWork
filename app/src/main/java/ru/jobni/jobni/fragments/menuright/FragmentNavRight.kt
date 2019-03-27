package ru.jobni.jobni.fragments.menuright

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentNavRightBinding
import ru.jobni.jobni.model.menu.right.RepositoryFilters.makeNavigationListFilters
import ru.jobni.jobni.utils.menuleft.NavRVAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentNavRight : Fragment() {

    private lateinit var navRecyclerView: RecyclerView
    private lateinit var navAdapter: NavRVAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentNavRightBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nav_right, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        navRecyclerView = binding.rvNavRight

        buildRecyclerView()

        return view
    }

    private fun buildRecyclerView() {

        navAdapter = NavRVAdapter(makeNavigationListFilters(viewModel.getHeaderList().value!!), activity!!)
        navAdapter.setHasStableIds(true)
        navRecyclerView.adapter = navAdapter

        navAdapter.setOnGroupClickListener(object : OnGroupClickListener {
            override fun onGroupClick(flatPos: Int): Boolean {
                when(flatPos) {
                    0 -> viewModel.setFragmentLaunch("Summary")
                    1 -> viewModel.setFragmentLaunch("ReviewsUser")
                    2 -> viewModel.setFragmentLaunch("ProfileUser")
                    else -> viewModel.setFragmentLaunch("Summary")
                }
                viewModel.setOpenDrawerLeft(false)
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
