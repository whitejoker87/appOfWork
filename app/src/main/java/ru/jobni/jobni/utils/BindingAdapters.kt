package ru.jobni.jobni.utils


import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.jobni.jobni.viewmodel.MainViewModel

@BindingMethods(
        BindingMethod(
                type = BindingAdapters::class,
                attribute = "onNavigationItemSelected",
                method = "setOnNavigationItemSelectedListener"
        )
)

object BindingAdapters {

    //открываем дравлер(пока не работает)
    @BindingAdapter("openDrawer")
    @JvmStatic
    fun onOpenDrawer(drawerLayout: DrawerLayout, gravity: Int) {
        //if (gravity == 0) drawerLayout.closeDrawer(GravityCompat.END)
        drawerLayout.openDrawer(gravity)
    }

    //блокировка дравлеров
    @BindingAdapter("setDrawerLockMode")
    @JvmStatic
    fun setDrawerLockMode(drawerLayout: DrawerLayout, mode: Int) {
        drawerLayout.setDrawerLockMode(mode)
    }

    //слушаем нижнее меню(пока не работает)
    @BindingAdapter("onNavigationItemSelected")
    @JvmStatic
    fun setOnNavigationItemSelectedListener(
            view: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener
    ) {
        view.setOnNavigationItemSelectedListener(listener)
    }

    //слушаем ресайклвью карточек
    @BindingAdapter("onScrollListener")
    @JvmStatic
    fun setOnScrollListener(view: RecyclerView, listener: RecyclerView.OnScrollListener) {
        view.addOnScrollListener(listener)
    }

    //слушаем строку поиска
    @BindingAdapter("onQueryTextListener")
    @JvmStatic
    fun setQueryTextListener(view: SearchView, listener: SearchView.OnQueryTextListener) {
        view.setOnQueryTextListener(listener)
    }

    //изменение в строке поиска
    @BindingAdapter("query")
    @JvmStatic
    fun query(view: SearchView, query: String) {
        view.setQuery(query, true)
    }

    //обработка клика списка из поиска
    @BindingAdapter("onListViewItemClickListener")
    @JvmStatic
    fun setOnItemClickListener(view: ListView, listener: AdapterView.OnItemClickListener) {
        view.onItemClickListener = listener
    }

    //для ресайклвью карточек
    @BindingAdapter("fixedSize")
    @JvmStatic
    fun fixedSize(view: RecyclerView, fixSize: Boolean) {
        view.setHasFixedSize(fixSize)
    }

    //преобразование для отображения з/п в карточках
    @BindingAdapter("formattedTextSalary")
    @JvmStatic
    fun setFormattedText(view: TextView, text: String) {
        val sb = StringBuffer(text)
        when {
            text.length < 3 -> {
                //do nothing
            }
            text.length == 4 -> sb.insert(1, " ")
            text.length == 6 -> sb.insert(3, " ")
            else -> sb.insert(2, " ")
        }
        view.text = sb.toString()
    }

    // Пример 1
    // в хмл объявляются 2 переменных и передаются в адаптер
    // Текстовое значение для setFragmentLaunch берется из файла @strings
    // Нажатие кнопки с закрытием в левом меню (авторизация)
    @BindingAdapter("clickNavMenuBtnAuth", "authBtn")
    @JvmStatic
    fun onClickNavMenuBtnAuth(view: View, clickNavMenuBtnAuth: MainViewModel, authBtn: String) {
        view.setOnClickListener {
            clickNavMenuBtnAuth.setFragmentLaunch(authBtn)
            clickNavMenuBtnAuth.setOpenDrawerLeft(false)
        }
    }

    // Пример 2
    // Из хмл передаются в адаптер только вьюмодель, вся обработка выполняется в адаптере
    // Нажатие кнопки с закрытием в левом меню (регистрация)
    @BindingAdapter("clickNavMenuBtnReg")
    @JvmStatic
    fun onClickNavMenuBtnReg(view: View, clickNavMenuBtnReg: MainViewModel) {
        view.setOnClickListener {
            clickNavMenuBtnReg.setFragmentLaunch("Registration")
            clickNavMenuBtnReg.setOpenDrawerLeft(false)
        }
    }

    //преобразование для сисков в карточках
    @BindingConversion
    @JvmStatic
    fun convertListToString(list: List<String>): String {
        val sb = StringBuilder()
        for (str in list) {
            if (sb.isNotEmpty()) sb.append(", ")
            sb.append(str)
        }
        return sb.toString()
    }


//    @BindingAdapter("app:onNavigationItemSelected")
//    fun setOnNavigationItemSelected(
//        view: BottomNavigationView, listener: BottomNavigationView
//    ) {
//        view.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//                when (item.itemId) {
//                    ru.jobni.jobni.R.id.bottom_menu_search -> {
//                        //setFragment(FragmentMain())
//                        return true
//                    }
//                    ru.jobni.jobni.R.id.bottom_menu_notification -> {
//                        return true
//                    }
//                    ru.jobni.jobni.R.id.bottom_menu_chat -> {
//                        return true
//                    }
//                    ru.jobni.jobni.R.id.bottom_menu_profile -> {
//                        //popup.show()
//                        return true
//                    }
//                }
//                return false
//            }
//
//        })
//
//    }
}
