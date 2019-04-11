package ru.jobni.jobni

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.UserDictionary
import android.provider.UserDictionary.Words.APP_ID
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
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
import com.vk.api.sdk.ui.VKWebViewAuthActivity
import com.vk.api.sdk.ui.VKWebViewAuthActivity.Companion.REDIRECT_URL
import kotlinx.android.synthetic.main.nav_left.*
import org.telegram.passport.TelegramPassport
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
import ru.jobni.jobni.fragments.api.discord.AuthenticationDialogDiscord
import ru.jobni.jobni.fragments.api.discord.AuthenticationListenerDiscord
import ru.jobni.jobni.fragments.api.discord.FragmentAuthDiscordUser
import ru.jobni.jobni.fragments.api.discord.FragmentAuthDiscordUserLogged
import ru.jobni.jobni.fragments.api.facebook.FragmentAuthFBUser
import ru.jobni.jobni.fragments.api.facebook.FragmentAuthFBUserLogged
import ru.jobni.jobni.fragments.api.google.FragmentAuthGoogleUser
import ru.jobni.jobni.fragments.api.google.FragmentAuthGoogleUserLogged
import ru.jobni.jobni.fragments.api.instagram.AuthenticationDialogInstagram
import ru.jobni.jobni.fragments.api.instagram.AuthenticationListenerInstagram
import ru.jobni.jobni.fragments.api.instagram.FragmentAuthInstagramUser
import ru.jobni.jobni.fragments.api.instagram.FragmentAuthInstagramUserLogged
import ru.jobni.jobni.fragments.api.mailru.AuthenticationDialogMailru
import ru.jobni.jobni.fragments.api.mailru.AuthenticationListenerMailru
import ru.jobni.jobni.fragments.api.mailru.FragmentAuthMailruUser
import ru.jobni.jobni.fragments.api.mailru.FragmentAuthMailruUserLogged
import ru.jobni.jobni.fragments.api.ok.FragmentAuthOKUser
import ru.jobni.jobni.fragments.api.ok.FragmentAuthOKUserLogged
import ru.jobni.jobni.fragments.api.reg.AttachPhotoBottomSheetDialogFragment
import ru.jobni.jobni.fragments.api.reg.FragmentReg
import ru.jobni.jobni.fragments.api.telegram.FragmentAuthTelegramUser
import ru.jobni.jobni.fragments.api.telegram.FragmentAuthTelegramUserLogged
import ru.jobni.jobni.fragments.menuleft.*
import ru.jobni.jobni.utils.menuleft.NavPALeftAuthOff
import ru.jobni.jobni.utils.menuleft.NavPALeftAuthOn
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel
import ru.ok.android.sdk.Odnoklassniki
import ru.ok.android.sdk.util.OkAuthType
import ru.ok.android.sdk.util.OkScope

class MainActivity : AppCompatActivity() {

    private val SET_FOCUS: String = "SetFocus"
    private val SET_CARDS: String = "SetCards"

    private val WRITE_REQUEST_CODE = 0
    private val CAMERA_REQUEST = 0
    private val GALLERY_REQUEST = 1
    private val VK_REQUEST = 1

    // -------------- YOUR APP DATA GOES HERE(for OK) ------------
    private val APP_ID = "1277635072"
    private val APP_KEY = "CBALCICNEBABABABA"
    private val REDIRECT_URL = "okauth://ok1277635072"
    // -------------- YOUR APP DATA ENDS -----------------


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
                if (viewModelAuth.isUserAuthid().value == true
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
                "AuthInstagramUser" -> setFragment(FragmentAuthInstagramUser())
                "AuthInstagramUserLogged" -> setFragment(FragmentAuthInstagramUserLogged())
                "AuthDiscordUser" -> setFragment(FragmentAuthDiscordUser())
                "AuthDiscordUserLogged" -> setFragment(FragmentAuthDiscordUserLogged())
                "AuthMailruUser" -> setFragment(FragmentAuthMailruUser())
                "AuthMailruUserLogged" -> setFragment(FragmentAuthMailruUserLogged())
                "AuthTelegramUser" -> setFragment(FragmentAuthTelegramUser())
                "AuthTelegramUserLogged" -> setFragment(FragmentAuthTelegramUserLogged())
                "RegUserMail" -> regViewModel.setTypeAddRegFragment("mail")
                "RegUserPhone" -> regViewModel.setTypeAddRegFragment("phone")
                "RegSocial" -> regViewModel.setTypeAddRegFragment("soc")
                "RegOK" -> regViewModel.setTypeAddRegFragment("soc")
                "RegInst" -> regViewModel.setTypeAddRegFragment("soc")
                "RegTel" -> regViewModel.setTypeAddRegFragment("soc")
                "RegGoogle" -> regViewModel.setTypeAddRegFragment("soc")
                "RegFB" -> regViewModel.setTypeAddRegFragment("soc")
                "RegMailRu" -> regViewModel.setTypeAddRegFragment("soc")
                "RegDiscord" -> regViewModel.setTypeAddRegFragment("soc")
                "RegMic" -> regViewModel.setTypeAddRegFragment("soc")
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

        regViewModel.getActivityLaunch().observe(this, Observer {
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
            } else regViewModel.openCamera()
        })

        viewModelAuth.isUserAuthid().observe(this, Observer {
            setFragmentReturnBackStack()
            closeKeyboard()
        })

        viewModelAuth.isVkAuthStart().observe(this, Observer {
            VK.login(this, arrayListOf())
        })

        regViewModel.getSocialRegStart().observe(this, Observer {
            if (it) {
                when (viewModelMain.getSocialLaunch().value) {
                    "RegSocial" -> VK.login(this, arrayListOf())
                    "RegOK" -> Odnoklassniki.createInstance(this, APP_ID, APP_KEY)
                        .requestAuthorization(this, REDIRECT_URL, OkAuthType.ANY, OkScope.VALUABLE_ACCESS)
                    /*Ответ не обрабатывается*/
                    "RegInst" -> {
                        val authenticationDialogInstagram =
                            AuthenticationDialogInstagram(this, object : AuthenticationListenerInstagram {
                                override fun onTokenReceived(code: String) {
                                    regViewModel.convertInstagramCode(code)
                                }
                            })
                        authenticationDialogInstagram.setCancelable(true)
                        authenticationDialogInstagram.show()
                    }
//                    "RegTel" -> regViewModel.setBtnUserLogged("tel")
//                    "RegGoogle" -> regViewModel.setBtnUserLogged("google")
//                    "RegFB" -> regViewModel.setBtnUserLogged("fb")/*Иным способом выводиться*/
                    "RegMailRu" -> {
                        val authenticationDialogMailru =
                            AuthenticationDialogMailru(this, object : AuthenticationListenerMailru {
                                override fun onTokenReceived(accessToken: String, vid: String) {
                                    regViewModel.sendSocialData(vid, "mailru", accessToken)
                                }
                            })
                        authenticationDialogMailru.setCancelable(true)
                        authenticationDialogMailru.show()
                    }
                    "RegDiscord" -> {
                        val authenticationDialogDiscord =
                            AuthenticationDialogDiscord(this, object : AuthenticationListenerDiscord {
                                override fun onTokenReceived(code: String) {
                                    regViewModel.convertDiscordCode(code)
                                }
                            })
                        authenticationDialogDiscord.setCancelable(true)
                        authenticationDialogDiscord.show()
                    }
//                    "RegMic" -> regViewModel.setBtnUserLogged("mic")
                }
            }
        })

        regViewModel.getResultReg1Success().observe(this, Observer {
            if (it) {
                when (viewModelMain.getSocialLaunch().value) {
                    "RegUserMail" -> regViewModel.postBindEmail()
                    "RegUserPhone" -> regViewModel.postBindPhone()
                }
            }
        })

        regViewModel.isPhotoDialogEnabled().observe(this, Observer { aBoolean ->
            if (aBoolean != null) if (aBoolean) photoDialogBottomSheetEnable()
        })

        regViewModel.getPhotoLaunch().observe(this, Observer {
            if (it != null) {
                regViewModel.setPhotoDialogEnabled(false)
                when (it) {
                    "camera" -> {
                        regViewModel.setPhotoLaunch("")
                        val permissions =
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(permissions, WRITE_REQUEST_CODE)
                        } else
                            regViewModel.openCamera()
                    }
                    "gallery" -> {
                        regViewModel.setPhotoLaunch("")
                        val photoPickerIntent = Intent(Intent.ACTION_PICK)
                        photoPickerIntent.type = "image/*"
                        startActivityForResult(photoPickerIntent, GALLERY_REQUEST)

//                        val pickPhoto = Intent(
//                            Intent.ACTION_PICK,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                        )
//                        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                        startActivityForResult(pickPhoto, GALLERY_REQUEST)
                    }
                    else -> {
                    }
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
                regViewModel.openCamera()
                //regViewModel.setPrivilegesForFileDone(true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*for telegram*/
        if (requestCode == 105) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Telegram login ok", Toast.LENGTH_SHORT).show();
            } else if (resultCode == TelegramPassport.RESULT_ERROR) {
                AlertDialog.Builder(this)
                    .setTitle("error!")
                    .setMessage(data!!.getStringExtra("error"))
                    .setPositiveButton("OKNORM", null)
                    .show();
            } else Toast.makeText(this, "cancel login telega", Toast.LENGTH_SHORT).show()
        }

        else if (resultCode == RESULT_OK) {

            /* VK callback block */
            val callback = object : VKAuthCallback {
                override fun onLogin(token: VKAccessToken) {

                    val accessToken = token.accessToken

                    val userLogin = token.userId

                    if (viewModelMain.getSocialLaunch().value.equals("RegSocial")) {
                        regViewModel.sendSocialData(userLogin.toString(), "vk", accessToken)
                    } else if (viewModelAuth.isVkAuthStart().value == true) {
                        viewModelAuth.onAuthVKClick(userLogin.toString(), "vk", accessToken)
                    }
                }

                override fun onLoginFailed(errorCode: Int) {}

            }
            if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
                super.onActivityResult(requestCode, resultCode, data)
            }

            //val uri: Uri? = null
            when (requestCode) {
                GALLERY_REQUEST -> {
                    regViewModel.setOutputPhotoUri(data!!.data!!)
                    regViewModel.regContactPhotoSend()
                }
                CAMERA_REQUEST -> {
                    regViewModel.setOutputPhotoUri(regViewModel.getOutputPhotoUri().value!!)
                    regViewModel.regContactPhotoSend()
                }
//                VK_REQUEST -> {
//
//                }
            }
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

    fun photoDialogBottomSheetEnable() {
        val bottomSheetDialogFragment = AttachPhotoBottomSheetDialogFragment()
        bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
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