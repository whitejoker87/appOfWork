package ru.jobni.jobni.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.jobni.jobni.R
import ru.jobni.jobni.model.VacancyEntity
import ru.jobni.jobni.model.network.vacancy.CardVacancyDetail
import ru.jobni.jobni.model.network.vacancy.Detail
import java.util.*


class CardRVAdapter(private val vacancyList: ArrayList<VacancyEntity>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0

    private var clickListener: OnItemClickListener? = null
    private var mExpandedPosition = -1
    private var previousExpandedPosition = -1

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onEyeClick(v: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.c_card_vacancy_close, parent, false)
        return ItemViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemViewHolder) {

            val isExpanded = position === mExpandedPosition

            viewHolder.btnLess.visibility = if (isExpanded) View.VISIBLE else View.GONE
            viewHolder.expandConstraintLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

            viewHolder.btnExpand.isActivated = isExpanded

            if (isExpanded)
                previousExpandedPosition = position

            viewHolder.btnExpand.setOnClickListener {
                mExpandedPosition = if (isExpanded) -1 else position
                cardExpand(position)
                notifyItemChanged(previousExpandedPosition)
                notifyItemChanged(position)
            }

            populateItemRows(viewHolder, position)
        }
    }

    override fun getItemCount(): Int {
        return vacancyList.size
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
        return vacancyList[position].id.toLong()
    }

    private class ItemViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        var eyeImage: ImageView = itemView.findViewById(R.id.eye)
        var vacancyNameText: TextView = itemView.findViewById(R.id.name)
        var companyNameText: TextView = itemView.findViewById(R.id.company_name)
        var salaryLevelNewbieText: TextView = itemView.findViewById(R.id.newbie)
        var salaryLevelExperiencedText: TextView = itemView.findViewById(R.id.experienced)
        var workFormatText: TextView = itemView.findViewById(R.id.work_format_text)
        var employmentListText: TextView = itemView.findViewById(R.id.employment_format_text)
        var competenceListText: TextView = itemView.findViewById(R.id.required_competencies_text)
        var btnExpand: Button = itemView.findViewById(R.id.btn_expand)
        var btnLess: Button = itemView.findViewById(R.id.btn_less)

        var expandConstraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraint_layout_expand)
        var companyDescriptionText: TextView = itemView.findViewById(R.id.сompany_description_text)
        var vacancyDescriptionText: TextView = itemView.findViewById(R.id.vacancy_description_text)
        var requirementsDescriptionText: TextView = itemView.findViewById(R.id.requirements_description_text)
        var dutiesDescriptionText: TextView = itemView.findViewById(R.id.duties_description_text)

        init {
            itemView.setOnClickListener {
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position)
                    }
                }
            }

            eyeImage.setOnClickListener { v ->
                if (listener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEyeClick(v, position)
                    }
                }
            }
        }
    }

    private fun populateItemRows(viewHolder: ItemViewHolder, position: Int) {
        val item = vacancyList[position]

        viewHolder.vacancyNameText.text = item.vacancyName
        viewHolder.companyNameText.text = item.companyName
        viewHolder.salaryLevelNewbieText.text = convertWithSpaces(item.salaryLevelNewbie)
        viewHolder.salaryLevelExperiencedText.text = convertWithSpaces(item.salaryLevelExperienced)
        viewHolder.workFormatText.text = item.formatOfWork
        viewHolder.employmentListText.text = item.employmentList.toString()
            .replace("[", "", true).replace("]", "", true)
        viewHolder.competenceListText.text = item.competenceList.toString()
            .replace("[", "", true).replace("]", "", true)
        viewHolder.companyDescriptionText.text = item.companyDescription
        viewHolder.vacancyDescriptionText.text = item.vacancyDescription
        viewHolder.requirementsDescriptionText.text = item.requirementsDescription
        viewHolder.dutiesDescriptionText.text = item.dutiesDescription
    }

    private fun convertWithSpaces(item: String): String {
        val sb = StringBuffer(item)
        when {
            item.length < 3 -> {
                //do nothing
            }
            item.length == 4 -> sb.insert(1, " ")
            item.length == 6 -> sb.insert(3, " ")
            else -> sb.insert(2, " ")
        }
        return sb.toString()
    }

    private fun cardExpand(position: Int) {
        val requestID: Int = vacancyList[position].id

        Retrofit.api?.loadVacancyCard(requestID, requestID)?.enqueue(object : Callback<CardVacancyDetail> {
            override fun onResponse(@NonNull call: Call<CardVacancyDetail>, @NonNull response: Response<CardVacancyDetail>) {
                if (response.body() != null) {

                    val resultList: Detail = response.body()!!.detail

                    val newObj: VacancyEntity = vacancyList[position].copy(
                        companyDescription = resultList.company_description,
                        vacancyDescription = resultList.description,
                        requirementsDescription = resultList.requirements,
                        dutiesDescription = resultList.duties
                    )
                    vacancyList.removeAt(position)
                    vacancyList.add(position, newObj)
                    notifyItemChanged(position)
                }
            }

            override fun onFailure(@NonNull call: Call<CardVacancyDetail>, @NonNull t: Throwable) {
            }
        })
    }
}
