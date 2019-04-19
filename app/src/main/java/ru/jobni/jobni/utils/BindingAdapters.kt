package ru.jobni.jobni.utils

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leinardi.android.speeddial.SpeedDialView
import com.squareup.picasso.Picasso
import ru.jobni.jobni.R
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

    //преобразование для списков в карточках
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

    @BindingAdapter("imageUrl", "errorImage")
    @JvmStatic
    fun loadPhotoPartsList(view: ImageView, uri: Uri, errorImage: Drawable) {
        Picasso.get()
                .load(uri)
                //.resize(160, 160)
                .fit()
                .rotate(270F)//для поворота в нормальную ориентацию
                .placeholder(errorImage)
                .error(errorImage)
                .into(view)
    }

    // https://stackoverflow.com/questions/47335090/safeunbox-cannot-be-inverted/47337166#47337166
    // This solution can be replicated to any property like visibility,
    // enabled etc. and to any boxed primitive like Integer, Float etc.
    @BindingAdapter("android:checked")
    @JvmStatic
    fun setChecked(checkableView: CompoundButton, isChecked: Boolean?) {
        checkableView.isChecked = isChecked ?: false
    }

    // You can also provide the value to be used in case your LiveData value is null like this:
    // And call it like this:
    //
    //<CheckBox
    //    ...
    //    android:checked='@{viewModel.value}'
    //    app:nullValue="@{false}"
    //    />
    @BindingAdapter(value = ["android:checked", "nullValue"], requireAll = false)
    @JvmStatic
    fun setChecked(checkableView: CompoundButton, isChecked: Boolean?, nullValue: Boolean) {
        checkableView.isChecked = isChecked ?: nullValue
    }

    //слушаем нажатие на fab в 3 реге
    @BindingAdapter("onClickSpeedDialItem")
    @JvmStatic
    fun setOnClickItemListener(view: SpeedDialView, listener: SpeedDialView.OnActionSelectedListener) {
        view.setOnActionSelectedListener(listener)
    }

    //для подсказок контакта 3 рега
    @BindingAdapter("hintContact")
    @JvmStatic
    fun hintContact(view: EditText, typeContact: String) {
        when(typeContact) {
            "email" -> view.hint = "name@domain.ru"
            "phone" -> view.hint = "+79991234567"
            "skype" -> view.hint = "login of skype"
            "home_phone" -> view.hint = "+78219876543"
            else -> view.hint = "это мне неведомо"
        }
    }

    @BindingAdapter("phaseNumber", "reg2ready", "reg3ready")
    @JvmStatic
    fun clickableRegPhase(view: LinearLayout, phaseNumber: Int, reg2ready: Boolean, reg3ready: Boolean) {
        when(phaseNumber) {
            0 -> view.isClickable = true
            1 -> view.isClickable = reg2ready
            2 -> view.isClickable = reg3ready
        }
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
