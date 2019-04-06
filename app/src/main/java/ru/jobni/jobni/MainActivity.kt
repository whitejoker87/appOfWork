package ru.jobni.jobni

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.tabs.TabLayout
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import kotlinx.android.synthetic.main.nav_left.*
import ru.jobni.jobni.databinding.ActivityMainBinding
import ru.jobni.jobni.fragments.*
import ru.jobni.jobni.fragments.api.auth.FragmentAuth
import ru.jobni.jobni.fragments.api.auth.mail.FragmentAuthMailUser
import ru.jobni.jobni.fragments.api.auth.mail.FragmentAuthMailUserLogged
import ru.jobni.jobni.fragments.api.auth.mail.FragmentAuthMailUserLoggedChangeMail
import ru.jobni.jobni.fragments.api.auth.mail.FragmentAuthMailUserLoggedChangePass
import ru.jobni.jobni.fragments.api.auth.phone.FragmentAuthPhoneUser
import ru.jobni.jobni.fragments.api.auth.phone.FragmentAuthPhoneUserLogged
import ru.jobni.jobni.fragments.api.auth.vk.FragmentAuthVKUser
import ru.jobni.jobni.fragments.api.auth.vk.FragmentAuthVKUserLogged
import ru.jobni.jobni.fragments.api.facebook.FragmentAuthFBUser
import ru.jobni.jobni.fragments.api.facebook.FragmentAuthFBUserLogged
import ru.jobni.jobni.fragments.api.google.FragmentAuthGoogleUser
import ru.jobni.jobni.fragments.api.google.FragmentAuthGoogleUserLogged
import ru.jobni.jobni.fragments.api.ok.FragmentAuthOKUser
import ru.jobni.jobni.fragments.api.ok.FragmentAuthOKUserLogged
import ru.jobni.jobni.fragments.api.reg.FragmentReg
import ru.jobni.jobni.fragments.menuleft.*
import ru.jobni.jobni.utils.menuleft.NavPALeftAuthOff
import ru.jobni.jobni.utils.menuleft.NavPALeftAuthOn
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel

class MainActivity : AppCompatActivity() {

    private val SET_FOCUS: String = "SetFocus"
    private val SET_CARDS: String = "SetCards"

    private val WRITE_REQUEST_CODE = 0
    private val CAMERA_REQUEST = 0

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var popup: PopupMenu
    private lateinit var drawer: DrawerLayout

//    private val expandableListAdapter: ExpandableListAdapter by lazy {
//        ExpandableListAdapter(applicationContext, viewModel.getHeaderList().value!!, viewModel.getChildList().value!!)
//    }
    //private lateinit var expandableListView: ExpandableListView

    private val viewModelMain: MainViewModel by lazy {
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
        binding.viewmodelmain = viewModelMain
        drawer = binding.drawerLayout

        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        bottomNavigationView = binding.menuBottom
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //expandableListView = binding.expListInclude.exp_list_view

        popup = PopupMenu(this@MainActivity, findViewById(R.id.bottom_menu_profile))
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.bottom_profile_not_logged_in, popup.menu)
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.reg_bottom_not_logged -> viewModelMain.setFragmentLaunch("Registration")
                    R.id.auth_bottom_not_logged -> viewModelMain.setFragmentLaunch("Auth")
                }
                return true
            }
        })

        viewModelMain.loadRightMenuData()

        //viewModelMain.sPref = getSharedPreferences("firstLaunchSavedData", MODE_PRIVATE)
        viewModelMain.saveLaunchFlag(false)//отладка первого запуска true
        if (savedInstanceState == null) {
            setFragment(FragmentSplashScreen())
        }

//        viewModel.getHeaderList().observe(this, Observer { headerList ->
//            viewModel.loadChildList(headerList)
//        })

//        viewModel.getChildList().observe(this, Observer {
//            if (viewModel.getHeaderList().value != null) {
//                expandableListView.setAdapter(expandableListAdapter)
//            }
//        })

        checkDrawerOpenClose()
        leftMenuPAdapters()

        viewModelMain.isOpenDrawerRight().observe(this, Observer { isOpen ->
            if (isOpen) {
                drawer.openDrawer(GravityCompat.END)
                //ниже закрываем клавиатуру если открыта
                closeKeyboard()
            } else {
                drawer.closeDrawer(GravityCompat.END)
            }
        })

        viewModelMain.isOpenDrawerLeft().observe(this, Observer { isOpen ->
            if (isOpen) {
                if (viewModelAuth.isMailAuthid().value == true
                        || viewModelAuth.isFBAuthid().value == true
                        || viewModelAuth.isGoogleAuthid().value == true
                ) {
                    viewModelMain.setNoAuthRegVisible(false)
                    viewModelMain.setYesAuthRegVisible(true)
                    drawer.openDrawer(GravityCompat.START)
                    //ниже закрываем клавиатуру если открыта
                    closeKeyboard()
                } else {
                    /* Debug, чтобы видеть полное меню без авторизации
                    * setNoAuthRegVisible(false)
                    * setYesAuthRegVisible(true)*/
                    viewModelMain.setNoAuthRegVisible(true)
                    viewModelMain.setYesAuthRegVisible(false)
                    drawer.openDrawer(GravityCompat.START)
                    //ниже закрываем клавиатуру если открыта
                    closeKeyboard()
                }
            } else {
                drawer.closeDrawer(GravityCompat.START)
            }
        })

        viewModelMain.getFragmentLaunch().observe(this, Observer {
            when (it) {
                "Welcome" -> setFragmentNoBackStack(FragmentWelcome())
                "Intro" -> setFragmentNoBackStack(FragmentIntro())
                "Main_cards" -> setFragment(FragmentMain.newInstance(SET_CARDS))
                "Main_focus" -> setFragment(FragmentMain.newInstance(SET_FOCUS))
                "Vacancy" -> setFragment(FragmentVacancy())
                "VacancyCompany" -> setFragment(FragmentVacancyCompany())
                "SummaryUser" -> setFragment(FragmentSummaryUser())
                "ReviewsUser" -> setFragment(FragmentReviewsUser())
                "ReviewsOwner" -> setFragment(FragmentReviewsOwner())
                "ProfileUser" -> setFragment(FragmentProfileUser())
                "ProfileOwner" -> setFragment(FragmentProfileOwner())
                "FinanceUser" -> setFragment(FragmentFinanceUser())
                "CompanyAddAuthOn" -> setFragment(FragmentCompanyAddAuthOn())
                "CompanyAddAuthOff" -> setFragment(FragmentCompanyAddAuthOff())
                "CompanyVacancy" -> setFragment(FragmentCompanyVacancy())
                "CompanyFinance" -> setFragment(FragmentCompanyFinance())
                "Auth" -> setFragment(FragmentAuth())
                "Registration" -> setFragment(FragmentReg())
                "AuthMailUserLoggedChangePass" -> setFragment(FragmentAuthMailUserLoggedChangePass())
                "AuthMailUserLoggedChangeMail" -> setFragment(FragmentAuthMailUserLoggedChangeMail())
                else -> setFragment(FragmentWelcome())
            }
        })

        /*наблюдение за нажатием на кнопки регистрации/авторизации*/
        viewModelMain.getSocialLaunch().observe(this, Observer {
            when (it) {
                /*аутентификация запускаеться сразу тут*/
                "AuthMailUser" -> setFragment(FragmentAuthMailUser())
                "AuthMailUserLogged" -> setFragment(FragmentAuthMailUserLogged())
                "AuthFBUser" -> setFragment(FragmentAuthFBUser())
                "AuthFBUserLogged" -> setFragment(FragmentAuthFBUserLogged())
                "AuthGoogleUser" -> setFragment(FragmentAuthGoogleUser())
                "AuthGoogleUserLogged" -> setFragment(FragmentAuthGoogleUserLogged())
                "AuthOKUser" -> setFragment(FragmentAuthOKUser())
                "AuthOKUserLogged" -> setFragment(FragmentAuthOKUserLogged())
                "AuthVKUser" -> setFragment(FragmentAuthVKUser())
                "AuthVKUserLogged" -> setFragment(FragmentAuthVKUserLogged())
                "AuthPhoneUser" -> setFragment(FragmentAuthPhoneUser())
                "AuthPhoneUserLogged" -> setFragment(FragmentAuthPhoneUserLogged())
                "RegUserMail" -> regViewModel.setBtnUserLogged("mail")
                "RegUserPhone" -> regViewModel.setBtnUserLogged("phone")
                "RegUserOther" -> regViewModel.setTypeAddRegFragment("other")//временный вариант пока нет всех соцсетей
                "RegVK" -> regViewModel.setBtnUserLogged("vk")
                //"AuthVK" -> viewModelAuth.setBtnUserLogged("vk")
            }
        })

        /*наблюдение за изменением статуса кнопок регистрации*/
        regViewModel.getBtnUserLogged().observe(this, Observer {
            when (it) {
                /*регистрация сначала использует getSocialLaunch т.к. нужно сперва изменить цвет кнопки*/
                "mail" -> regViewModel.setTypeAddRegFragment("mail")
                "phone" -> regViewModel.setTypeAddRegFragment("phone")
                "vk" -> regViewModel.setTypeAddRegFragment("vk")
            }
        })

        viewModelMain.isBottomNavigationViewVisible().observe(this, Observer { isVisible ->
            if (isVisible) binding.menuBottom.visibility = View.VISIBLE
            else binding.menuBottom.visibility = View.GONE
        })

        viewModelMain.isDrawerRightLocked().observe(this, Observer { isLocked ->
            if (isLocked) binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            else binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        })

        viewModelMain.getActivityLaunch().observe(this, Observer {
            it?.let { intent ->
                startActivityForResult(intent, CAMERA_REQUEST)
            }
        })

        regViewModel.isPrivilegesForFileDone().observe(this, Observer {
            if (!it) {
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this, permissions, WRITE_REQUEST_CODE)
                }
            } else {
                //regViewModel.registration()
            }
        })

        viewModelAuth.isMailAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        viewModelAuth.isFBAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        viewModelAuth.isGoogleAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        viewModelAuth.isOKAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        viewModelAuth.isVKAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        viewModelAuth.isPhoneAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        viewModelAuth.isInstagramAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        regViewModel.isVkRegStart().observe(this, Observer {
            if (it) {
                VK.login(this, arrayListOf())
            }
        })

        regViewModel.getResultReg1Success().observe(this, Observer {
            if (it) {
                when (viewModelMain.getSocialLaunch().value) {
                    "mail" -> regViewModel.postBindEmail()
                }
            }
        })
    }

    private fun leftMenuPAdapters() {
        val fragmentAdapterAuthOn = NavPALeftAuthOn(supportFragmentManager, this)
        view_pager_nav_left_auth_on.adapter = fragmentAdapterAuthOn
        tab_layout_nav_left_auth_on.setupWithViewPager(view_pager_nav_left_auth_on)

        tab_layout_nav_left_auth_on.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    //0 -> Toast.makeText(applicationContext, "1", Toast.LENGTH_LONG).show()
                    1 -> viewModelMain.loadLeftMenuOwnerData()
                    //1 -> viewModelMain.loadLeftMenuOwnerDataBalance()
                }
            }
        })

        val fragmentAdapterAuthOff = NavPALeftAuthOff(supportFragmentManager, this)
        view_pager_nav_left_auth_off.adapter = fragmentAdapterAuthOff
        tab_layout_nav_left_auth_off.setupWithViewPager(view_pager_nav_left_auth_off)

        tab_layout_nav_left_auth_off.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    //0 -> Toast.makeText(applicationContext, "3", Toast.LENGTH_LONG).show()
                    //1 -> Toast.makeText(applicationContext, "4", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun checkDrawerOpenClose() {
        drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerClosed(drawerView: View) {}

            override fun onDrawerOpened(drawerView: View) {
                if (drawer.isDrawerOpen(GravityCompat.START)) viewModelMain.setOpenDrawerLeft(true)
                else if (drawer.isDrawerOpen(GravityCompat.END)) viewModelMain.setOpenDrawerRight(true)
            }
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

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                regViewModel.setPrivilegesForFileDone(true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //val uri: Uri? = null
            when (requestCode) {
                //GALLERY_REQUEST -> uri = data.data
                CAMERA_REQUEST -> viewModelMain.setOutputPhotoUri(viewModelMain.getOutputPhotoUri().value!!)
            }
        }

        /* VK callback block */
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                val accessToken = token.accessToken
                val userLogin = token.userId

                regViewModel.btnVKClick(userLogin.toString(), "vk", accessToken)
            }

            override fun onLoginFailed(errorCode: Int) {}
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.bottom_menu_search -> {
                    viewModelMain.setFragmentLaunch("Main_focus")
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

    private fun setFragmentReturnBackStack() {
        supportFragmentManager.popBackStack()
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
//                                is SocialPacket -> headerList.add("Социальный пакет")
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