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


class RecyclerAdapter(private val vacancyList: ArrayList<VacancyEntity>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var clickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onEyeClick(v: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    class ViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.c_card_vacancy_close, parent, false)
        return ViewHolder(v, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = vacancyList[position]

        holder.vacancyNameText.text = currentItem.vacancyName
        holder.companyNameText.text = currentItem.companyName
        holder.salaryLevelNewbieText.text = currentItem.salaryLevelNewbie
        holder.salaryLevelExperiencedText.text = currentItem.salaryLevelExperienced
        holder.workFormatText.text = currentItem.formatOfWork
        holder.employmentListText.text = currentItem.employmentList.toString()
            .replace("[", "", true).replace("]", "", true)
        holder.competenceListText.text = currentItem.competenceList.toString()
            .replace("[", "", true).replace("]", "", true)
    }

    override fun getItemCount(): Int {
        return vacancyList.size
    }
}
