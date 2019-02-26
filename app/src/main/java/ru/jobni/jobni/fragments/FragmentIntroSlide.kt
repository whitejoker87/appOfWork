package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.jobni.jobni.R

class FragmentIntroSlide : Fragment() {

    private var param1: Int = 0
    private var listener: OnClickBtnStartListener? = null
    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(param1, container, false);
        if (param1 == R.layout.intro_05) {
            btnStart = view.findViewById(R.id.start)
            btnStart.setOnClickListener { onButtonStartPressed() }
        }
        return view
    }

    private fun onButtonStartPressed() {
        listener?.onClickBtnStart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickBtnStartListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnClickBtnStartListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

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
    interface OnClickBtnStartListener {
        fun onClickBtnStart()
    }

    companion object {
        @JvmStatic
        fun newInstance(num: Int) =
            FragmentIntroSlide().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, num)
                }
            }

        private const val ARG_PARAM1 = "param1"
    }
}
