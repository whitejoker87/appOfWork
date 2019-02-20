package ru.jobni.jobni.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import ru.jobni.jobni.model.IntroSlide
import java.util.ArrayList

class IntroViewModel : ViewModel() {

//    val currentName: MutableLiveData<String> by lazy {
//        MutableLiveData<String>()
//    }

    val listSlides: MutableLiveData<List<IntroSlide>> by lazy {
        MutableLiveData<List<IntroSlide>>()
    }

    fun setListSlides(list: List<IntroSlide>) {
        listSlides.setValue(list)
    }

//    fun getListSlides(): LiveData<List<IntroSlide>> {
//        if (listSlides.getValue() == null) listSlides.setValue(ArrayList<IntroSlide>())
//        return listSlides
//    }

    private var sPref: SharedPreferences? = null

    fun getsPref(): SharedPreferences? {
        return sPref
    }

    fun setsPref(sPref: SharedPreferences) {
        this.sPref = sPref
    }

}
