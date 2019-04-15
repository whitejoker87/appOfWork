package ru.jobni.jobni.fragments.api.reg

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.R
import ru.jobni.jobni.utils.RegContactsRecyclerAdapter
import ru.jobni.jobni.viewmodel.RegViewModel

class FragmentRegThree : Fragment() {

    private val viewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private lateinit var binding: ru.jobni.jobni.databinding.CRegistration03Binding

    private lateinit var recycler_reg_contacts: RecyclerView
    private lateinit var adapter: RegContactsRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_registration_03, container, false)
        val view = binding.root;
        binding.viewmodel = viewModel
        viewModel.getContactsForReg3()
        recycler_reg_contacts = binding.rvRegContact
        adapter = RegContactsRecyclerAdapter(activity as Context)
        recycler_reg_contacts.adapter = adapter

        viewModel.getRegContacts().observe(this, Observer {
            it?.let { adapter.contacts = it }
        })

        return view
    }
}
