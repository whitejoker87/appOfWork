package ru.jobni.jobni.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.R
import ru.jobni.jobni.model.VacancyEntity
import java.util.*


class CardRVAdapter(private val vacancyList: ArrayList<VacancyEntity>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0

    private var clickListener: OnItemClickListener? = null

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

    private class ItemViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        var eyeImage: ImageView = itemView.findViewById(R.id.eye)
        var vacancyNameText: TextView = itemView.findViewById(R.id.name)
        var companyNameText: TextView = itemView.findViewById(R.id.company_name)
        var salaryLevelNewbieText: TextView = itemView.findViewById(R.id.newbie)
        var salaryLevelExperiencedText: TextView = itemView.findViewById(R.id.experienced)
        var workFormatText: TextView = itemView.findViewById(R.id.work_format_text)
        var employmentListText: TextView = itemView.findViewById(R.id.employment_format_text)
        var competenceListText: TextView = itemView.findViewById(R.id.required_competencies_text)

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

    }

    private fun convertWithSpaces(item: String): String {
        val sb = StringBuffer(item)
        when {
            item.length < 5 -> {
                //do nothing
            }
            item.length == 6 -> sb.insert(3, " ")
            else -> sb.insert(2, " ")
        }
        return sb.toString()
    }
}
