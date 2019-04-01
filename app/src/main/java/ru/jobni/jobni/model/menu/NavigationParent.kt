package ru.jobni.jobni.model.menu

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup

class NavigationParent(
        title: String,
        items: List<NavigationChild>,
        val iconResId: Int
) : MultiCheckExpandableGroup(title, items)