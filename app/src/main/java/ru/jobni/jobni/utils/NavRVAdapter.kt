package ru.jobni.jobni.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Checkable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.ListItemChildBinding
import ru.jobni.jobni.databinding.ListItemParentBinding
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent

class NavRVAdapter(groups: List<NavigationParent>, private val context: FragmentActivity) :
        CheckableChildRecyclerViewAdapter<NavRVAdapter.ViewHolderParent, NavRVAdapter.ViewHolderChild>(groups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ViewHolderParent {
        val view = LayoutInflater.from(parent.context)
        val bindingParent: ListItemParentBinding =
                DataBindingUtil.inflate(view, R.layout.list_item_parent, parent, false)
        return ViewHolderParent(bindingParent)
    }

    override fun onCreateCheckChildViewHolder(parent: ViewGroup?, viewType: Int): ViewHolderChild {
        val view = LayoutInflater.from(parent?.context)
        val bindingChild: ListItemChildBinding = DataBindingUtil.inflate(view, R.layout.list_item_child, parent, false)
        return ViewHolderChild(bindingChild)
    }

    override fun onBindCheckChildViewHolder(
            holderChild: ViewHolderChild,
            flatPosition: Int,
            group: CheckedExpandableGroup,
            childIndex: Int
    ) {
        val child = group.items[childIndex] as NavigationChild

        holderChild.bind(child)
        holderChild.setChildIcon(child)
    }

    override fun onBindGroupViewHolder(
            holderParent: ViewHolderParent,
            flatPosition: Int,
            group: ExpandableGroup<*>
    ) {
        holderParent.bind(group)
        holderParent.setParentIcon(group)
    }

    class ViewHolderChild(val binding: ListItemChildBinding) : CheckableChildViewHolder(binding.root) {

        private val childIcon = binding.listItemChildIcon
        private val childChecked = binding.listItemChildCheck

        fun bind(child: NavigationChild) {
            binding.child = child
            binding.executePendingBindings()
        }

        override fun getCheckable(): Checkable? {
            return childChecked
        }

        fun setChildIcon(child: NavigationChild) {
            childIcon.setBackgroundResource(child.iconResId)
        }
    }

    inner class ViewHolderParent(val binding: ListItemParentBinding) : GroupViewHolder(binding.root) {

        private val arrow = binding.listItemParentArrow
        private val parentIcon = binding.listItemParentIcon

        fun bind(parent: ExpandableGroup<*>) {
            binding.parent = parent as NavigationParent
            binding.executePendingBindings()
        }

        fun setParentIcon(parent: ExpandableGroup<*>) {
            if (parent is NavigationParent) {
                parentIcon.setBackgroundResource(parent.iconResId)
            }
        }

        override fun expand() {
            animateExpand()
        }

        override fun collapse() {
            animateCollapse()
        }

        private fun animateExpand() {
            val rotate = RotateAnimation(360f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 300
            rotate.fillAfter = true
            arrow.animation = rotate
        }

        private fun animateCollapse() {
            val rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 300
            rotate.fillAfter = true
            arrow.animation = rotate
        }
    }
}
