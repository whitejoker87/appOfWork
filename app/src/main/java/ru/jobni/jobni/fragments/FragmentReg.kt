package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentRegRecyclerBinding
import ru.jobni.jobni.utils.RegRecyclerAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentReg : Fragment() {

    private lateinit var recycler_reg: RecyclerView
    private lateinit var adapter: RegRecyclerAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentRegRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reg_recycler, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        recycler_reg = binding.recyclerReg
        adapter = RegRecyclerAdapter(activity as Context)
        recycler_reg.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.setToolbarVisible(true)
        viewModel.setBottomNavigationViewVisible(true)
        viewModel.setSearchViewVisible(false)
        viewModel.setDrawerRightLocked(false)
    }
}
