package ru.jobni.jobni.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ru.jobni.jobni.R
import ru.jobni.jobni.model.SuggestionEntity
import java.util.*

class SearchLVAdapter(private val mContext: Context, private val suggestionList: ArrayList<SuggestionEntity>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
        return suggestionList.size
    }

    override fun getItem(position: Int): SuggestionEntity {
        return suggestionList[position]
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(position: Int): Boolean {
        return !suggestionList[position].suggestionName.equals(mContext.getString(R.string.search_view_suggestions_empty), ignoreCase = true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder {
        internal var suggestionName: TextView? = null
    }

    override fun getView(position: Int, viewLV: View?, parent: ViewGroup): View {
        var view = viewLV
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.c_search_item, null)
            holder.suggestionName = view!!.findViewById(R.id.tv_search_item)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        holder.suggestionName!!.text = suggestionList[position].suggestionName
        return view
    }
}
