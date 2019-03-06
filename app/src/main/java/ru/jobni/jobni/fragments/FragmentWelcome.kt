package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.jobni.jobni.R

class FragmentWelcome : Fragment() {

    companion object {
        fun newInstance() = FragmentWelcome()
    }

    private lateinit var buttonSearch: Button
    private lateinit var buttonFind: Button

    private val SET_FOCUS: String = "SetFocus"
    private val SET_CARDS: String = "SetCards"

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var searchViewMain: SearchView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        buttonSearch = view.findViewById(R.id.search_welcome) as Button
        buttonFind = view.findViewById(R.id.search_button_welcome) as Button

        bottomNavigationView = activity!!.findViewById(R.id.menu_bottom)
        constraintLayout = activity!!.findViewById(R.id.constraint_layout_menu_top)

        drawerLayout = activity!!.findViewById(R.id.drawer_layout)

        searchViewMain = activity!!.findViewById(R.id.search_main)

        initElements()

        return view
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.visibility = View.VISIBLE
        constraintLayout.visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        searchViewMain.visibility = View.GONE
    }

    private fun setFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment)
                ?.addToBackStack(null)
                ?.commit()
    }

    private fun initElements() {
        buttonFind.setOnClickListener {
            setFragment(FragmentMain.newInstance(SET_CARDS))
        }

        buttonSearch.setOnClickListener {
            setFragment(FragmentMain.newInstance(SET_FOCUS))
        }
    }
}
