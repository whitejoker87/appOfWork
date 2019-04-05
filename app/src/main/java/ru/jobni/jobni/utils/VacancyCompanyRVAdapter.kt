package ru.jobni.jobni.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CCardVacancyCompanyCloseBinding
import ru.jobni.jobni.model.CompanyVacancyEntity
import ru.jobni.jobni.viewmodel.MainViewModel


class VacancyCompanyRVAdapter(context: Context) : RecyclerView.Adapter<VacancyCompanyRVAdapter.CardViewHolder>() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(context as FragmentActivity).get(MainViewModel::class.java)
    }

    private val VIEW_TYPE_ITEM = 0

    var vacancies: MutableList<CompanyVacancyEntity> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyCompanyRVAdapter.CardViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding: CCardVacancyCompanyCloseBinding = DataBindingUtil.inflate(view, R.layout.c_card_vacancy_company_close, parent, false)
        return CardViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(viewHolder: VacancyCompanyRVAdapter.CardViewHolder, position: Int) {
        viewHolder.bind(vacancies[position])
    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun getItemId(position: Int): Long {
        return vacancies[position].id.toLong()
    }

    class CardViewHolder(val binding: CCardVacancyCompanyCloseBinding, val viewmodel: MainViewModel) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: CompanyVacancyEntity) {
            binding.vacancy = vacancy
            binding.position = adapterPosition
            binding.viewmodel = viewmodel
            binding.executePendingBindings()
        }
    }
}
