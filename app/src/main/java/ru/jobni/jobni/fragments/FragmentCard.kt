package ru.jobni.jobni.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CCardVacancyOpenMapOpenBinding
import ru.jobni.jobni.model.RepositoryVacancyEntity
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentCard : Fragment() {

    val SERVER_LOAD_CARD_DELAY: Long = 10000 // 10 sec. Время ожидания положительного ответа от АПИ

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: CCardVacancyOpenMapOpenBinding

    private val repository: RepositoryVacancyEntity = RepositoryVacancyEntity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.c_card_vacancy_open_map_open, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        // Бля биндинга нужно не пустое значение
        // В нашем случаи берем первую вакансию в списке
        binding.vacancy = repository.getVacancy().value?.get(0)

        binding.viewmodel = viewModel

        viewModel.isCardExpandResponse().observe(this, Observer {
            showProgressBar()
            viewModel.setLoadCardVisible(false)
            viewModel.setLoadCardFailVisible(false)

            if (!it) {
                val handler = Handler()
                handler.postDelayed({
                    // Если ответа от АПИ нет, скрываем ProgressBar и выводим сообщение об ошибке
                    hideProgressBar()
                    viewModel.setLoadCardFailVisible(true)
                }, SERVER_LOAD_CARD_DELAY)
            }
            else {
                viewModel.setLoadCardVisible(true)
            }
        })

        viewModel.getModelVacancy().observe(this, Observer { vacancy ->
            vacancy?.let { binding.vacancy = it.vacancyList[viewModel.cardPosition] }
        })

        return view
    }

    private fun showProgressBar() {
        binding.loadIcon.searchProgressBar.animate().setDuration(200).alpha(1f).start()
    }

    private fun hideProgressBar() {
        binding.loadIcon.searchProgressBar.animate().setDuration(200).alpha(0f).start()
    }
}
