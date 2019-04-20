package ru.jobni.jobni.fragments.api.reg

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.databinding.FragmentRegRecyclerBinding
import ru.jobni.jobni.utils.RegRecyclerAdapter
import ru.jobni.jobni.viewmodel.AuthViewModel
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel


class FragmentReg : Fragment() {

    private lateinit var recycler_reg: RecyclerView
    private lateinit var adapter: RegRecyclerAdapter

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProviders.of(activity!!).get(AuthViewModel::class.java)
    }

    private lateinit var binding: FragmentRegRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, ru.jobni.jobni.R.layout.fragment_reg_recycler, container, false)

        binding.lifecycleOwner = this

        val view = binding.root
        binding.viewmodel = mainViewModel

        regViewModel.clearRegValues()//сброс заполеннных полей реги

        recycler_reg = binding.recyclerReg
        //recycler_reg.itemAnimator!!.changeDuration = 0
//        val animator = recycler_reg.itemAnimator
//        if (animator is SimpleItemAnimator) {
//            (animator as SimpleItemAnimator).supportsChangeAnimations = false
//        }
        adapter = RegRecyclerAdapter(activity as Context)
        recycler_reg.adapter = adapter

        //переход на типы реги первого этапа
        regViewModel.getTypeAddRegFragment().observe(this, Observer {
            adapter.typeReg = it
        })

        //переход на 2 этап реги
        regViewModel.getResultReg2Success().observe(this, Observer {
            if (it) {
                recycler_reg.findViewHolderForAdapterPosition(1)?.itemView?.performClick()
            }
        })

        //переход на 3 этап реги
        regViewModel.getResultReg3Success().observe(this, Observer {
            if (it) {
                recycler_reg.findViewHolderForAdapterPosition(2)?.itemView?.performClick()
            }
        })

        //переход на окно авторизации после окончания реги
        regViewModel.getResultReg4Success().observe(this, Observer {
            if (it) {
                authViewModel.setUserAuthid(true)
                //authViewModel.setBtnUserLogged(typeProvider)
                mainViewModel.setFragmentLaunch("Main_focus")
                regViewModel.setResultReg4Success(false)//пеерключаем флаг для доступа к регистрации
            }
        })

        regViewModel.isUpdateReg1().observe(this, Observer{
            if (it){
                adapter.notifyDataSetChanged()
                regViewModel.setUpdateReg1(false)
            }
        } )

        return view
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.setToolbarVisible(true)
        mainViewModel.setBottomNavigationViewVisible(true)
        mainViewModel.setSearchViewVisible(false)
        mainViewModel.setDrawerRightLocked(false)
    }
}
