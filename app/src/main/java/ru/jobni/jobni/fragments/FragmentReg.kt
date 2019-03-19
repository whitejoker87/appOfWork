package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.R
import ru.jobni.jobni.utils.RegRecyclerAdapter

class FragmentReg : Fragment() {

    private lateinit var recycler_reg: RecyclerView
    private lateinit var adapter: RegRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reg_recycler, container, false)

        recycler_reg = view.findViewById(R.id.recycler_reg)
        adapter = RegRecyclerAdapter(activity as Context)
        recycler_reg.layoutManager = LinearLayoutManager(activity)
        recycler_reg.adapter = adapter

        return view
    }
}
