package ru.jobni.jobni.fragments.menuleft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentNavLeftUserBinding
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.left.RepositoryUser.makeNavigationListUserAuthOff
import ru.jobni.jobni.utils.NavRVAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentNavLeftUserAuthOff : Fragment() {

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

        buildRecyclerView()

        return view
    }

    private fun buildRecyclerView() {

        navAdapter = NavRVAdapter(makeNavigationListUserAuthOff(), activity!!)
        navRecyclerView.adapter = navAdapter

        navAdapter.setOnGroupClickListener(object : OnGroupClickListener {
            override fun onGroupClick(flatPos: Int): Boolean {
                if (flatPos == 1) {
                    viewModel.setFragmentLaunch("Registration")
                    viewModel.setOpenDrawerLeft(false)
                    return false
                }

                return false
            }
        })

        navAdapter.setChildClickListener { v, checked, group, childIndex ->
            val child = group.items[childIndex] as NavigationChild

            if (childIndex == 0) {
                Toast.makeText(context, "" + child.id, Toast.LENGTH_SHORT).show()
                viewModel.setOpenDrawerLeft(false)
            }

            if (childIndex == 1) {
                Toast.makeText(context, "" + child.id, Toast.LENGTH_SHORT).show()
                viewModel.setOpenDrawerLeft(false)
            }

            if (childIndex == 2) {
                Toast.makeText(context, "" + child.id, Toast.LENGTH_SHORT).show()
                viewModel.setOpenDrawerLeft(false)
            }

            if (childIndex == 3) {
                Toast.makeText(context, "" + child.id, Toast.LENGTH_SHORT).show()
                viewModel.setOpenDrawerLeft(false)
            }
        }
    }
}
