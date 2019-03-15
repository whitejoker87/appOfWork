package ru.jobni.jobni.utils


import com.google.android.material.textfield.TextInputLayout
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.jobni.jobni.model.RepositoryVacancyEntity

@BindingMethods(
    BindingMethod(
        type = BindingAdapters::class,
        attribute = "app:onNavigationItemSelected",
        method = "setOnNavigationItemSelectedListener"
    )
)

object BindingAdapters {

    @BindingAdapter("app:openDrawer")
    @JvmStatic fun openDrawer(drawerLayout: DrawerLayout, gravity: Int) {
        //if (gravity == 0) drawerLayout.closeDrawer(GravityCompat.END)
        drawerLayout.openDrawer(gravity)
    }

    @BindingAdapter("app:setDrawerLockMode")
    @JvmStatic fun setDrawerLockMode(drawerLayout: DrawerLayout, mode: Int) {
        drawerLayout.setDrawerLockMode(mode)
    }

    @BindingAdapter("app:onNavigationItemSelected")
    @JvmStatic fun setOnNavigationItemSelectedListener(
        view: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener
    ) {
        view.setOnNavigationItemSelectedListener(listener)
    }

    @BindingAdapter("app:onScrollListener")
    @JvmStatic fun setOnScrollListener(view: RecyclerView, listener: RecyclerView.OnScrollListener) {
        view.addOnScrollListener(listener)
    }

    @BindingAdapter("app:onQueryTextListener")
    @JvmStatic fun setQueryTextListener(view: SearchView, listener: SearchView.OnQueryTextListener) {
        view.setOnQueryTextListener(listener)
    }

    @BindingAdapter("app:query")
    @JvmStatic fun query(view: SearchView, query: String) {
        view.setQuery(query, true)
    }

    @BindingAdapter("app:onListViewItemClickListener")
    @JvmStatic fun setOnItemClickListener(view: ListView, listener: AdapterView.OnItemClickListener) {
        view.onItemClickListener = listener
    }

    @BindingAdapter("app:fixedSize")
    @JvmStatic fun fixedSize(view: RecyclerView, fixSize: Boolean) {
        view.setHasFixedSize(fixSize)
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
