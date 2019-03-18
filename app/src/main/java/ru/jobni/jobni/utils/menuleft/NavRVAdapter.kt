package ru.jobni.jobni.utils.menuleft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import ru.jobni.jobni.R
import ru.jobni.jobni.model.menuleft.NavigationParent
import ru.jobni.jobni.viewmodel.MainViewModel

class NavRVAdapter(groups: List<ExpandableGroup<*>>, private val context: FragmentActivity) :
        ExpandableRecyclerViewAdapter<NavRVAdapter.ViewHolderParent, NavRVAdapter.ViewHolderChild>(groups) {

    private val viewModel: MainViewModel = ViewModelProviders.of(context).get(MainViewModel::class.java)

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ViewHolderParent {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_parent, parent, false)
        return ViewHolderParent(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChild {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_child, parent, false)
        return ViewHolderChild(view)
    }

    override fun onBindChildViewHolder(
        holderChild: ViewHolderChild,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val child = (group as NavigationParent).items[childIndex]

        holderChild.setChildName(child.name)
        holderChild.setChildIcon(child.iconResId)

        holderChild.itemView.setOnClickListener {
            if (child.name.contains("Вакансии") && childIndex == 0) {
                viewModel.setFragmentLaunch("CompanyVacancy")
                viewModel.setOpenDrawerLeft(false)
            }
            if (child.name.contains("Отзывы") && childIndex == 1) {
                viewModel.setFragmentLaunch("ReviewsOwner")
                viewModel.setOpenDrawerLeft(false)
            }
            if (child.name.contains("Профиль") && childIndex == 2) {
                viewModel.setFragmentLaunch("ProfileOwner")
                viewModel.setOpenDrawerLeft(false)
            }

            else {
                Toast.makeText(context, child.name, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindGroupViewHolder(
        holderParent: ViewHolderParent, flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        holderParent.setParentTitle(group)
    }

    class ViewHolderChild(itemView: View) : ChildViewHolder(itemView) {

        private val childTextView: TextView
        private val childIcon: ImageView

        init {
            childTextView = itemView.findViewById<View>(R.id.list_item_child_name) as TextView
            childIcon = itemView.findViewById<View>(R.id.list_item_child_icon) as ImageView
        }

        fun setChildName(name: String) {
            childTextView.text = name
        }

        fun setChildIcon(icon: Int) {
            childIcon.setImageResource(icon)
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
