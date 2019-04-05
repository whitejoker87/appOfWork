package ru.jobni.jobni.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.MainActivity
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.RegPhaseRecyclerItemBinding
import ru.jobni.jobni.fragments.api.reg.*
import ru.jobni.jobni.viewmodel.RegViewModel


class RegRecyclerAdapter(/*private val mObjects: List<String>,*/ private val mContext: Context) :
    RecyclerView.Adapter<RegRecyclerAdapter.ViewHolder>() {

    private val FINAL_OPACITY = 0.3f
    private val START_OPACITY = 1f

    private val ANIMATION_TIME = 500
    private val TYPE_ITEM = 0
    private val TYPE_DATE = 1
    private val TYPE_TRANSACTION = 2
    private val TYPE_PENDING = 3

    private val viewModel: RegViewModel by lazy {
        ViewModelProviders.of(mContext as FragmentActivity).get(RegViewModel::class.java)
    }

    // Get fragment manager inside our fragment
    val fragmentManager = (mContext as MainActivity).supportFragmentManager

    var typeReg = ""
        set(value) {
            field = value
            notifyItemChanged(viewModel.getNumberOfVisibleItemReg().value!!)
        }

    private val mAnimationUp: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up)
    private val mAnimationDown: Animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_down)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ru.jobni.jobni.databinding.RegPhaseRecyclerItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.reg_phase_recycler_item, parent, false)
        return ViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (position == 0 && (viewModel.getNumberOfVisibleItemReg().value in arrayOf(0,-1))) {
                setFragmentInItem(holder, position)
            }

        // Remember that RecyclerView does not have onClickListener, you should implement it
        holder.itemView.setOnClickListener(object : View.OnClickListener {

            // Method that could us an unique id
            //val uniqueId = View.generateViewId()

            override fun onClick(view: View) {
                // Hide details
                // regFragmentPhaseContainer object could be checked on inner class ViewHolder
                if (holder.regFragmentPhaseContainer.isShown) {
//                    holder.regFragmentPhaseContainer.startAnimation(mAnimationDown)
//                    Handler().postDelayed({
                        holder.regFragmentPhaseContainer.visibility = View.GONE
//                    }, 1000)
                    holder.regFragmentPhaseTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                } else {
//                    holder.regFragmentPhaseContainer.startAnimation(mAnimationUp)
                    setFragmentInItem(holder, position)
//                    holder.regFragmentPhaseContainer.visibility = View.VISIBLE
                    notifyDataSetChanged()
                }
            }
        })

        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 3
    }

    fun setFragmentInItem(holder: ViewHolder, position: Int) {
        viewModel.setNumberOfVisibleItemReg(position)
        // Delete previous added fragment
        val currentContainerId = holder.regFragmentPhaseContainer.id
        // Get the current fragment
        val oldFragment = fragmentManager.findFragmentById(currentContainerId)
        if (oldFragment != null) {
            // Delete fragmet from ui, do not forget commit() otherwise no action
            // is going to be observed
            fragmentManager.beginTransaction().remove(oldFragment).commit()
        }
        // In order to be able of replacing a fragment on a recycler view
        // the target container should always have a different id ALWAYS
        val newContainerId = position + 1
        // Set the new Id to our know fragment container
        holder.regFragmentPhaseContainer.id = newContainerId

        // Just for Testing we are going to create a new fragment according
        // if the view position is pair one fragment type is created, if not
        // a different one is used
        val f: Fragment
        when (position) {
            0 -> {
                when(typeReg) {
                    "" -> f = FragmentRegEmpty()
                    "mail" -> f = FragmentRegOneMail()//регистрация почта
                    "phone" -> f = FragmentRegOnePhone()//регистрация телефон
                    "other" -> f = FragmentRegOneOther()//временный вариант открытия всех соцсетей
                    "vk" -> f = FragmentRegOneOther.newInstance("vk")//регистрация вк
                    else -> f = FragmentRegOneMail()
                }
            }
            1 -> f = FragmentRegTwo()
            2 -> f = FragmentRegThree()
            else -> f = FragmentRegOneMail()
        }

        // Then just replace the recycler view fragment as usually
        fragmentManager.beginTransaction().replace(newContainerId, f).commit()

        // Once all fragment replacement is done we can show the hidden container
        //holder.regFragmentPhaseContainer.visibility = View.VISIBLE
    }

    class ViewHolder(private val binding: RegPhaseRecyclerItemBinding, private val viewModel: RegViewModel) :
        RecyclerView.ViewHolder(binding.root) {
        var regFragmentPhaseContainer: FrameLayout = binding.containerRegRvItem
        val regFragmentPhaseTitle: TextView = binding.tvHeaderReg

        init {
//                this.regFragmentPhaseTitle.setOnClickListener(View.OnClickListener {
//                    viewModel.setNumberOfVisibleItemReg(adapterPosition)
//                    notifyDataSetChanged()
//                })

            // This linear layout status is GONE in order to avoid the view to use space
            // even when it is not seen, when any element selected the Adapter will manage the
            // behavior for showing the layout - container
            //regFragmentPhaseContainer.visibility = View.GONE
        }

        fun bind(position: Int) {
            binding.position = position
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

    }
}
