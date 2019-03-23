package ru.jobni.jobni

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.menu_right.view.*
import kotlinx.android.synthetic.main.nav_header_left.*
import ru.jobni.jobni.databinding.ActivityMainBinding
import ru.jobni.jobni.fragments.*
import ru.jobni.jobni.fragments.menuleft.*
import ru.jobni.jobni.utils.ExpandableListAdapter
import ru.jobni.jobni.utils.menuleft.NavPALeft
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel

class MainActivity : AppCompatActivity() {

    private val SET_FOCUS: String = "SetFocus"
    private val SET_CARDS: String = "SetCards"

    private val WRITE_REQUEST_CODE = 0

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var popup: PopupMenu
    private lateinit var drawer: DrawerLayout

    private val expandableListAdapter: ExpandableListAdapter by lazy {
        ExpandableListAdapter(applicationContext, viewModel.getHeaderList().value!!, viewModel.getChildList().value!!)
    }
    private lateinit var expandableListView: ExpandableListView

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(this).get(RegViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        drawer = binding.drawerLayout

        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        bottomNavigationView = binding.menuBottom
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        expandableListView = binding.expListInclude.exp_list_view

        popup = PopupMenu(this@MainActivity, findViewById(R.id.bottom_menu_profile))
        val inflater = popup.getMenuInflater()
        inflater.inflate(R.menu.bottom_profile_not_logged_in, popup.getMenu())
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.reg_bottom_not_logged -> setFragment(FragmentReg())
                    R.id.auth_bottom_not_logged -> setFragment(FragmentAuth())
                }
                return true
            }
        })

        //viewModel.sPref = getSharedPreferences("firstLaunchSavedData", MODE_PRIVATE)
        viewModel.saveLaunchFlag(true)//отладка первого запуска true
        if (savedInstanceState == null) {
            setFragment(FragmentSplashScreen())
        }

        viewModel.getHeaderList().observe(this, Observer { headerList ->
            viewModel.loadChildList(headerList)
        })

        viewModel.getChildList().observe(this, Observer {
            if (viewModel.getHeaderList().value != null) {
                expandableListView.setAdapter(expandableListAdapter)
            }
        })

        viewModel.isOpenDrawerRight().observe(this, Observer { isOpen ->
            if (isOpen) {
                drawer.openDrawer(GravityCompat.END)//todo
                //ниже закрываем клавиатуру если открыта
                val view = this.currentFocus
                view?.let { v ->
                    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
                }
            } else {
                drawer.closeDrawer(GravityCompat.END)
            }
        })

        viewModel.isOpenDrawerLeft().observe(this, Observer { isOpen ->
            if (isOpen) {
                drawer.openDrawer(GravityCompat.START)//todo
                //ниже закрываем клавиатуру если открыта
                val view = this.currentFocus
                view?.let { v ->
                    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
                }
            } else {
                drawer.closeDrawer(GravityCompat.START)
            }
        })

        viewModel.getFragmentLaunch().observe(this, Observer { fragmentType ->
            when (fragmentType) {
                "Welcome" -> setFragmentNoBackStack(FragmentWelcome())
                "Intro" -> setFragmentNoBackStack(FragmentIntro())
                "Main_cards" -> setFragment(FragmentMain.newInstance(SET_CARDS))
                "Main_focus" -> setFragment(FragmentMain.newInstance(SET_FOCUS))
                "Card" -> setFragment(FragmentCard())
                "Summary" -> setFragment(FragmentSummary())
                "ReviewsUser" -> setFragment(FragmentReviewsUser())
                "ReviewsOwner" -> setFragment(FragmentReviewsOwner())
                "ProfileUser" -> setFragment(FragmentProfileUser())
                "ProfileOwner" -> setFragment(FragmentProfileOwner())
                "CompanyAdd" -> setFragment(FragmentCompanyAdd())
                "CompanyVacancy" -> setFragment(FragmentCompanyVacancy())
                "Auth" -> setFragment(FragmentAuth())
                "AuthUser" -> setFragment(FragmentAuthUser())
                else -> setFragment(FragmentWelcome())
            }
        })

        viewModel.isBottomNavigationViewVisible().observe(this, Observer { isVisible ->
            if (isVisible) binding.menuBottom.visibility = View.VISIBLE
            else binding.menuBottom.visibility = View.GONE
        })

        viewModel.isDrawerRightLocked().observe(this, Observer { isLocked ->
            if (isLocked) binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            else binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        })

        regViewModel.isPrivilegesForFileDone().observe(this, Observer {
            if (!it) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this, permissions, WRITE_REQUEST_CODE)
                }
            } else {
                //regViewModel.registration()
            }
        })

        val fragmentAdapter = NavPALeft(supportFragmentManager, this)
        view_pager_nav_left.adapter = fragmentAdapter
        tab_layout_nav_left.setupWithViewPager(view_pager_nav_left)

        viewModelAuth.isAuthUser().observe(this, Observer {
            viewModel.setFragmentLaunch("Auth")
            closeKeyboard()
        })
    }

    override fun onBackPressed() {
        when {
            drawer.isDrawerOpen(GravityCompat.START) -> drawer.closeDrawer(GravityCompat.START)
            drawer.isDrawerOpen(GravityCompat.END) -> drawer.closeDrawer(GravityCompat.END)
            supportFragmentManager.backStackEntryCount > 0 -> supportFragmentManager.popBackStack()
            else -> super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                regViewModel.setPrivilegesForFileDone(true)
            }
        }
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.bottom_menu_search -> {
                    setFragment(FragmentMain())
                    return true
                }
                R.id.bottom_menu_notification -> {
                    return true
                }
                R.id.bottom_menu_chat -> {
                    return true
                }
                R.id.bottom_menu_profile -> {
                    popup.show()
                    return true
                }
            }
            return false
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun setFragmentNoBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }

    private fun closeKeyboard() {
        //скрываем клавиатуру
        val viewCloseButton = this.currentFocus
        viewCloseButton?.let { v ->
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

//    private fun openRightMenu() {
//        if (headerList.isEmpty()) {
//            Retrofit.api?.loadDetailVacancy()?.enqueue(object : Callback<DetailVacancy> {
//                override fun onResponse(@NonNull call: Call<DetailVacancy>, @NonNull response: Response<DetailVacancy>) {
//                    if (response.body() != null) {
//                        val (competence, languages, work_places, employment, format_of_work, field_of_activity, age_company, required_number_of_people, zarplata, social_packet, auto, raiting) = response.body()!!
//
//                        val detailList: MutableList<Any> = mutableListOf(
//                                competence,
//                                languages,
//                                work_places,
//                                employment,
//                                format_of_work,
//                                field_of_activity,
//                                age_company,
//                                required_number_of_people,
//                                zarplata,
//                                social_packet,
//                                auto,
//                                raiting
//                        )
//                        detailList.forEach { str: Any ->
//                            if (str is String) headerList.add(str)
//                            else when (str) {
//                                is Zarplata -> headerList.add("Зарплата")
//                                is Social_packet -> headerList.add("Социальный пакет")
//                                is Auto -> headerList.add("Авто")
//                                is Raiting -> headerList.add("Рейтинг")
//                            }
//                        }
//
//                        prepareListData()
//
//                        expandableListAdapter = ExpandableListAdapter(applicationContext, headerList, childList)
//                        expandableListView.setAdapter(expandableListAdapter)
//                    }
//                }
//
//                override fun onFailure(@NonNull call: Call<DetailVacancy>, @NonNull t: Throwable) {
//                    Toast.makeText(applicationContext, "Error in download for menu!", Toast.LENGTH_LONG).show()
//                }
//            })
//        }
//
//        drawer.openDrawer(GravityCompat.END)
//        //ниже закрываем клавиатуру если открыта
//        val view = this.currentFocus
//        view?.let { v ->
//            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
//        }
//    }
}