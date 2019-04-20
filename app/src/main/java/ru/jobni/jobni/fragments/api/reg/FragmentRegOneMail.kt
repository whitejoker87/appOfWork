package ru.jobni.jobni.fragments.api.reg

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.viewmodel.MainViewModel
import ru.jobni.jobni.viewmodel.RegViewModel

class FragmentRegOneMail : Fragment() {

    private val regViewModel: RegViewModel by lazy {
        ViewModelProviders.of(activity!!).get(RegViewModel::class.java)
    }

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: ru.jobni.jobni.databinding.CRegistration01MailBinding

    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_registration_01_mail, container, false)
        val view = binding.root

        binding.viewmodelreg = regViewModel
        binding.viewmodelmain = mainViewModel

        binding.mail.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (regViewModel.getRegMail().value!!.length > 0) binding.btnSendMailCode.visibility = View.VISIBLE
//                else binding.btnSendMailCode.visibility = View.GONE
                regViewModel.setUpdateReg1(true)
            }

        })

        binding.btnSendMailCode.setOnClickListener {
            View.OnClickListener {
                regViewModel.regOrBind("mail")
            }
        }

//        binding.includeSocialNetwork.btnUser.setOnClickListener {
//            viewModel
//        }

        /*временный костыль для авторизации после первого этапа регистрации*/
//        regViewModel.getResultReg1Success().observe(this, Observer {
//            regViewModel.postBindEmail()
//        })

//        regViewModel.getResultAuthSuccess().observe(this, Observer {
//            Toast.makeText(context, "Регистрация успешно проехала? ${it}", Toast.LENGTH_LONG).show()
//        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentRegOneMail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRegOneMail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
    }
}
