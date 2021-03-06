package ru.jobni.jobni.utils

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import ru.jobni.jobni.R


class ExpandableListAdapter(
    private val _context: Context, private val _listDataHeader: List<String> // header titles
    ,
    // child data in format of header title, child title
    private val _listDataChild: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        return this._listDataChild[this._listDataHeader[groupPosition]]!![childPosititon]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        var convertViewChild = convertView

        val childText = getChild(groupPosition, childPosition) as String

        if (convertViewChild == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewChild = infalInflater.inflate(R.layout.menu_right_item, null)
        }

        val txtListChild = convertViewChild!!
            .findViewById(R.id.lblListHeader) as TextView

        txtListChild.text = childText
        return convertViewChild
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this._listDataChild[this._listDataHeader[groupPosition]]!!
            .size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this._listDataHeader[groupPosition]
    }

    override fun getGroupCount(): Int {
        return this._listDataHeader.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var convertViewGroup = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertViewGroup == null) {
            val infalInflater = this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewGroup = infalInflater.inflate(R.layout.menu_right_item, null)
        }

        val lblListHeader = convertViewGroup!!
            .findViewById(R.id.lblListHeader) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle

        return convertViewGroup
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}