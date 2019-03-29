package ru.jobni.jobni.fragments.menuleft

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
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.FragmentVacancyActiveBinding
import ru.jobni.jobni.model.RepositoryVacancy
import ru.jobni.jobni.utils.CardRVAdapter
import ru.jobni.jobni.viewmodel.MainViewModel

class FragmentVacancyActive : Fragment() {

    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardRVAdapter
    private val repository: RepositoryVacancy = RepositoryVacancy

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentVacancyActiveBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vacancy_active, container, false)

        binding.lifecycleOwner = this

        val view = binding.root

        binding.viewmodel = viewModel

        cardRecyclerView = binding.rvVacancyCards

        cardAdapter = CardRVAdapter(activity as Context)

        buildCardsRecyclerView()
        cardAdapter.notifyDataSetChanged()

        viewModel.getModelVacancy().observe(this, Observer { vacancy ->
            vacancy?.let {
                cardAdapter.vacancies = repository.getVacancy().value!!
            }
        })

        return view
    }

    private fun buildCardsRecyclerView() {

        cardAdapter.setHasStableIds(true)
        cardRecyclerView.adapter = cardAdapter
    }
}


