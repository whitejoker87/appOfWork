package ru.jobni.jobni.fragments.api.reg

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentBottomSheetPhotoBinding
import ru.jobni.jobni.viewmodel.RegViewModel

class AttachPhotoBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetPhotoBinding

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private lateinit var tvCancel: TextView


//    private val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
//        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
//            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                dismiss()
//            }
//        }
//
//        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
//
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_photo, container, false)
        binding.setLifecycleOwner(this)
        val view = binding.root
        binding.viewmodelreg = regViewModel

        tvCancel = binding.tvBtnCancel

        //TODO: перенести в VM
        tvCancel.setOnClickListener { dismiss() }


        regViewModel.isPhotoDialogEnabled().observe(this, Observer {
            if (it != null) if (!it) dismiss()
        })
        return view
    }
}
