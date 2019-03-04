package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import ru.jobni.jobni.R

class FragmentWelcome : Fragment() {

    companion object {
        fun newInstance() = FragmentWelcome()
    }

    private lateinit var searchWelcome: ImageButton
    private lateinit var buttonWelcome: Button

    private val SET_FOCUS: String = "SetFocus"
    private val SET_CARDS: String = "SetCards"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        searchWelcome = view.findViewById(R.id.search_welcome) as ImageButton
        buttonWelcome = view.findViewById(R.id.search_button) as Button

        initElements(view)

        return view
    }

    private fun setFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment)
                ?.addToBackStack(null)
                ?.commit()
    }

    private fun initElements(view: View) {
        buttonWelcome.setOnClickListener {
            setFragment(FragmentMain.newInstance(SET_CARDS))
        }

        searchWelcome.setOnClickListener {
            setFragment(FragmentMain.newInstance(SET_FOCUS))
        }
    }
}
