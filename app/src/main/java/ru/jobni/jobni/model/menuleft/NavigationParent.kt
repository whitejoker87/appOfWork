package ru.jobni.jobni.model.menuleft

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class NavigationParent(
        title: String,
        items: List<NavigationChild>,
        val iconResId: Int) : ExpandableGroup<NavigationChild>(title, items) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NavigationParent) return false

        val parent = other as NavigationParent?

        return iconResId == parent!!.iconResId
    }

    override fun hashCode(): Int {
        return iconResId
    }
}

