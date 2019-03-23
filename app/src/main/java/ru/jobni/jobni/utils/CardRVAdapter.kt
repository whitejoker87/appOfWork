package ru.jobni.jobni.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.CCardVacancyCloseBinding
import ru.jobni.jobni.model.VacancyEntity
import ru.jobni.jobni.model.network.vacancy.CardVacancyDetail
import ru.jobni.jobni.model.network.vacancy.Detail
import ru.jobni.jobni.viewmodel.MainViewModel


class CardRVAdapter(context: Context) : RecyclerView.Adapter<CardRVAdapter.CardViewHolder>() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(context as FragmentActivity).get(MainViewModel::class.java)
    }

    private val VIEW_TYPE_ITEM = 0

    var vacancies: MutableList<VacancyEntity> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardRVAdapter.CardViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding: CCardVacancyCloseBinding =
            DataBindingUtil.inflate(view, R.layout.c_card_vacancy_close, parent, false)
        return CardViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(viewHolder: CardRVAdapter.CardViewHolder, position: Int) {
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

    class CardViewHolder(val binding: CCardVacancyCloseBinding, val viewmodel: MainViewModel) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vacancy: VacancyEntity) {
            binding.vacancy = vacancy
            binding.position = adapterPosition
            binding.viewmodel = viewmodel
            binding.executePendingBindings()
        }
    }

    private fun cardExpand(position: Int) {
        val requestID: Int = vacancies[position].id

        Retrofit.api?.loadVacancyCard(requestID, requestID)?.enqueue(object : Callback<CardVacancyDetail> {
            override fun onResponse(@NonNull call: Call<CardVacancyDetail>, @NonNull response: Response<CardVacancyDetail>) {
                if (response.body() != null) {

                    val resultList: Detail = response.body()!!.detail

                    val newObj: VacancyEntity = vacancies[position].copy(
                        companyDescription = resultList.company_description,
                        vacancyDescription = resultList.description,
                        requirementsDescription = resultList.requirements,
                        dutiesDescription = resultList.duties
                    )
                    vacancies.removeAt(position)
                    vacancies.add(position, newObj)
                    notifyItemChanged(position)
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancyDetail>, @NonNull t: Throwable) {
            }
        })
    }
}
