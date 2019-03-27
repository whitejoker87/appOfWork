package ru.jobni.jobni.utils.menuleft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.ListItemChildBinding
import ru.jobni.jobni.model.menu.NavigationChild
import ru.jobni.jobni.model.menu.NavigationParent
import ru.jobni.jobni.viewmodel.MainViewModel

class NavRVAdapter(groups: List<ExpandableGroup<*>>, private val context: FragmentActivity) :
        ExpandableRecyclerViewAdapter<NavRVAdapter.ViewHolderParent, NavRVAdapter.ViewHolderChild>(groups) {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(context).get(MainViewModel::class.java)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ViewHolderParent {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_parent, parent, false)
        return ViewHolderParent(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChild {
        val view = LayoutInflater.from(parent.context)
        val binding: ListItemChildBinding = DataBindingUtil.inflate(view, R.layout.list_item_child, parent, false)
        return ViewHolderChild(binding, viewModel)
    }

    override fun onBindChildViewHolder(
            holderChild: ViewHolderChild,
            flatPosition: Int,
            group: ExpandableGroup<*>,
            childIndex: Int
    ) {
        val child = (group as NavigationParent).items[childIndex]

        holderChild.bind(child, childIndex)
    }

    override fun onBindGroupViewHolder(
            holderParent: ViewHolderParent, flatPosition: Int,
            group: ExpandableGroup<*>
    ) {
        holderParent.setParentTitle(group)
    }

    class ViewHolderChild(val binding: ListItemChildBinding, val viewmodel: MainViewModel) : ChildViewHolder(binding.root) {

        fun bind(child: NavigationChild, childIndex: Int) {
            binding.position = childIndex
            binding.child = child
            binding.viewmodel = viewmodel
            binding.executePendingBindings()
        }
    }

    inner class ViewHolderParent(itemView: View) : GroupViewHolder(itemView) {

        private val parentName: TextView
        private val arrow: ImageView
        private val icon: ImageView

        init {
            parentName = itemView.findViewById<View>(R.id.list_item_parent_name) as TextView
            arrow = itemView.findViewById<View>(R.id.list_item_parent_arrow) as ImageView
            icon = itemView.findViewById<View>(R.id.list_item_parent_icon) as ImageView
        }

        fun setParentTitle(parent: ExpandableGroup<*>) {
            if (parent is NavigationParent) {
                parentName.text = parent.getTitle()
                icon.setBackgroundResource(parent.iconResId)
            }

            if (parent.items.isEmpty()) {
                arrow.visibility = View.GONE
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
