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

class FragmentRegAuth : Fragment() {

    private var param1: String? = null
    private lateinit var recycler_reg: RecyclerView
    private lateinit var adapter: RegRecyclerAdapter

//    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var layout = 0
        if (param1.equals("reg"))
            layout = R.layout.fragment_reg_recycler
        else if (param1.equals("auth"))
            layout = R.layout.c_authorization_mail
        val view = inflater.inflate(layout, container, false)

        recycler_reg = view.findViewById(R.id.recycler_reg)
        adapter = RegRecyclerAdapter(activity as Context)
        recycler_reg.layoutManager = LinearLayoutManager(activity)
        recycler_reg.adapter = adapter

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
                FragmentRegAuth().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }

        private const val ARG_PARAM1 = "param1"
    }
}
