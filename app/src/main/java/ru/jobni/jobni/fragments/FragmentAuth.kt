package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.jobni.jobni.R

class FragmentAuth : Fragment() {

//    private var param1: String? = null

//    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String) =
//                FragmentAuth().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                    }
//                }
//
//        private const val ARG_PARAM1 = "param1"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//        }
//
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.c_authorization_mail, container, false)
    }

//    override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        var layout = 0
//        if (param1.equals("reg"))
//            layout = R.layout.fragment_reg_recycler
//        else if (param1.equals("auth"))
//            layout = R.layout.c_authorization_mail
//        val view = inflater.inflate(layout, container, false)
//
//        recycler_reg = view.findViewById(R.id.recycler_reg)
//        adapter = RegRecyclerAdapter(activity as Context)
//        recycler_reg.layoutManager = LinearLayoutManager(activity)
//        recycler_reg.adapter = adapter
//
//        return view
//    }
}
