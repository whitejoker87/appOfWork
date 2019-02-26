package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import ru.jobni.jobni.R

class FragmentWelcome : Fragment() {

    companion object {
        fun newInstance() = FragmentWelcome()
    }

    private lateinit var searchWelcome: SearchView
    private lateinit var buttonWelcome: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        searchWelcome = view.findViewById(R.id.search_view_welcome) as SearchView
        buttonWelcome = view.findViewById(R.id.search_work) as Button

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
            setFragment(FragmentMain())
        }

        searchWelcome.setOnClickListener {
            setFragment(FragmentMain())
        }

        searchWelcome.setOnSearchClickListener {
            setFragment(FragmentMain())
        }

        searchWelcome.setOnQueryTextFocusChangeListener { _: View, _: Boolean ->
            setFragment(FragmentMain())
        }
    }
}
