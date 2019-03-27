package ru.jobni.jobni.utils


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
import ru.jobni.jobni.model.RepositoryVacancyEntity
import android.R.attr.name
import android.graphics.drawable.Drawable
import android.net.Uri
import com.squareup.picasso.Picasso


@BindingMethods(
    BindingMethod(
        type = BindingAdapters::class,
        attribute = "app:onNavigationItemSelected",
        method = "setOnNavigationItemSelectedListener"
    )
)

object BindingAdapters {

    //открываем дравлер(пока не работает)
    @BindingAdapter("app:openDrawer")
    @JvmStatic fun openDrawer(drawerLayout: DrawerLayout, gravity: Int) {
        //if (gravity == 0) drawerLayout.closeDrawer(GravityCompat.END)
        drawerLayout.openDrawer(gravity)
    }

    //блокировка дравлеров
    @BindingAdapter("app:setDrawerLockMode")
    @JvmStatic fun setDrawerLockMode(drawerLayout: DrawerLayout, mode: Int) {
        drawerLayout.setDrawerLockMode(mode)
    }

    //слушаем нижнее меню(пока не работает)
    @BindingAdapter("app:onNavigationItemSelected")
    @JvmStatic fun setOnNavigationItemSelectedListener(
        view: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener
    ) {
        view.setOnNavigationItemSelectedListener(listener)
    }

    //слушаем ресайклвью карточек
    @BindingAdapter("app:onScrollListener")
    @JvmStatic fun setOnScrollListener(view: RecyclerView, listener: RecyclerView.OnScrollListener) {
        view.addOnScrollListener(listener)
    }

    //слушаем строку поиска
    @BindingAdapter("app:onQueryTextListener")
    @JvmStatic fun setQueryTextListener(view: SearchView, listener: SearchView.OnQueryTextListener) {
        view.setOnQueryTextListener(listener)
    }

    //изменение в строке поиска
    @BindingAdapter("app:query")
    @JvmStatic fun query(view: SearchView, query: String) {
        view.setQuery(query, true)
    }

    //обработка клика списка из поиска
    @BindingAdapter("app:onListViewItemClickListener")
    @JvmStatic fun setOnItemClickListener(view: ListView, listener: AdapterView.OnItemClickListener) {
        view.onItemClickListener = listener
    }

    //для ресайклвью карточек
    @BindingAdapter("app:fixedSize")
    @JvmStatic fun fixedSize(view: RecyclerView, fixSize: Boolean) {
        view.setHasFixedSize(fixSize)
    }

    //преобразование для отображения з/п в карточках
    @BindingAdapter("app:formattedTextSalary")
    @JvmStatic fun setFormattedText(view: TextView, text: String) {
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

    //преобразование для сисков в карточках
    @BindingConversion
    @JvmStatic fun convertListToString(list: List<String>): String {
        val sb = StringBuilder()
        for (str in list) {
            if (sb.isNotEmpty()) sb.append(", ")
            sb.append(str)
        }
        return sb.toString()
    }

    @BindingAdapter("imageUrl", "errorImage")
    @JvmStatic fun loadPhotoPartsList(view: ImageView, uri: Uri, errorImage: Drawable) {
        Picasso.get()
            .load(uri)
            //.resize(50, 50)
            .placeholder(errorImage)
            .error(errorImage)
            .into(view)
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
