package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.jobni.jobni.R

class FragmentWelcome : Fragment() {

    companion object {
        fun newInstance() = FragmentWelcome()
    }

    private lateinit var searchWelcome: ImageButton
    private lateinit var buttonWelcome: Button

    private val SET_FOCUS: String = "SetFocus"
    private val SET_CARDS: String = "SetCards"

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        searchWelcome = view.findViewById(R.id.search_welcome) as ImageButton
        buttonWelcome = view.findViewById(R.id.search_button) as Button

        bottomNavigationView = activity!!.findViewById(R.id.menu_bottom)
        constraintLayout = activity!!.findViewById(R.id.constraint_layout_menu_top)

        drawerLayout = activity!!.findViewById(R.id.drawer_layout)

        initElements()

        return view
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.visibility = View.VISIBLE
        constraintLayout.visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun setFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment)
                ?.addToBackStack(null)
                ?.commit()
    }

    private fun initElements() {
        buttonWelcome.setOnClickListener {
            setFragment(FragmentMain.newInstance(SET_CARDS))
        }

        searchWelcome.setOnClickListener {
            setFragment(FragmentMain.newInstance(SET_FOCUS))
        }
    }
}
