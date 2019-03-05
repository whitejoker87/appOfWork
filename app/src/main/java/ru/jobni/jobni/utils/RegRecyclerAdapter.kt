package ru.jobni.jobni.utils

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.MainActivity
import ru.jobni.jobni.R
import ru.jobni.jobni.fragments.FragmentRegViewPagerOne
import ru.jobni.jobni.fragments.FragmentRegViewPagerThree
import ru.jobni.jobni.fragments.FragmentRegViewPagerTwo

import java.util.HashMap





class RegRecyclerAdapter(/*private val mObjects: List<String>,*/ private val mContext: Context) : RecyclerView.Adapter<RegRecyclerAdapter.ViewHolder>() {
    //private val TAG = RecyclerViewAdapter::class.java!!.getSimpleName()
//
//    private val FINAL_OPACITY = 0.3f
//    private val START_OPACITY = 1f
//
//    private val ANIMATION_TIME = 500
//    private val TYPE_ITEM = 0
//    private val TYPE_DATE = 1
//    private val TYPE_TRANSACTION = 2
//    private val TYPE_PENDING = 3
//
//    private val mElementTypes: HashMap<Int, Int>
//    //private val mCurrencySelected: Utils.CURRENCIES? // Which currency is already selected
//    private val mCurrencyFilter: Boolean // Defines if a currency is already selected to apply filter
//    private val mAnimationUp: Animation
//    private val mAnimationDown: Animation

    init {
//        mElementTypes = HashMap()
//        mCurrencyFilter = false
//        //mCurrencySelected = null
//        mAnimationUp = AnimationUtils.loadAnimation(mContext, ru.jobni.jobni.R.anim.slide_up)
//        mAnimationDown = AnimationUtils.loadAnimation(mContext, ru.jobni.jobni.R.anim.slide_down)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(ru.jobni.jobni.R.layout.reg_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val operation = mObjects[position]
        //holder.setAppUserActivity(userActivityOperation)
        val fragmentManager = (mContext as MainActivity).supportFragmentManager
        when(position) {
            0 -> {
                holder.iOperation.text = "1.Регистрационные данные"
                holder.iOperation.setTextColor(mContext.resources.getColor(R.color.white))
                holder.iDetailsContainer.visibility = View.VISIBLE
                fragmentManager.beginTransaction().replace(R.id.container_reg_rv_item, FragmentRegViewPagerOne()).commit()
            }
            1 -> holder.iOperation.text = "2.Основная информация"
            2 -> holder.iOperation.text = "3.Контактные данные"
        }

        // Remember that RecyclerView does not have onClickListener, you should implement it
        holder.itemView.setOnClickListener(object : View.OnClickListener {

            // Method that could us an unique id
            //val uniqueId = View.generateViewId()

            override fun onClick(view: View) {
                // Hide details
                // iDetailsContainer object could be checked on inner class ViewHolder
                if (holder.iDetailsContainer.isShown) {
                    holder.iDetailsContainer.visibility = View.GONE
                } else {
                    // Show details
                    // Get fragment manager inside our fragment
                    //val fragmentManager = (mContext as MainActivity).supportFragmentManager
                    val newContainerId = View.generateViewId()
                    // Delete previous added fragment
                    val currentContainerId = holder.iDetailsContainer.id
                    // Get the current fragment
                    val oldFragment = fragmentManager.findFragmentById(currentContainerId)
                    if (oldFragment != null) {
                        // Delete fragmet from ui, do not forget commit() otherwise no action
                        // is going to be observed
                        fragmentManager.beginTransaction().remove(oldFragment).commit()
                    }

                    // In order to be able of replacing a fragment on a recycler view
                    // the target container should always have a different id ALWAYS
                    //val newContainerId = uniqueId
                    // Set the new Id to our know fragment container
                    holder.iDetailsContainer.id = newContainerId

                    // Just for Testing we are going to create a new fragment according
                    // if the view position is pair one fragment type is created, if not
                    // a different one is used
                    val f: Fragment
                    when (position) {
                        0 -> f = FragmentRegViewPagerOne()
                        1 -> f = FragmentRegViewPagerTwo()
                        2 -> f = FragmentRegViewPagerThree()
                        else -> f = FragmentRegViewPagerOne()
                    }

                    // Then just replace the recycler view fragment as usually
                    fragmentManager.beginTransaction().replace(newContainerId, f).commit()

                    // Once all fragment replacement is done we can show the hidden container
                    holder.iDetailsContainer.visibility = View.VISIBLE
                }
            }
        })
//         Delete old fragment
//        val fragmentManager = (mContext as MainActivity).supportFragmentManager
//        val containerId = holder.iDetailsContainer.id// Get container id
//        val oldFragment = fragmentManager.findFragmentById(containerId)
//        if (oldFragment != null) {
//            fragmentManager.beginTransaction().remove(oldFragment).commit()
//        }
//
//        val newContainerId = View.generateViewId()
//        holder.iDetailsContainer.id = newContainerId// Set container id
//
//// Add new fragment
//        val fragment = FragmentRegViewPagerOne()
//        fragmentManager.beginTransaction().replace(newContainerId, fragment).commit()
    }

    override fun getItemCount(): Int {
        return 3
    }

    class ViewHolder(private val iView: View) : RecyclerView.ViewHolder(iView) {
//        private val iContainer: LinearLayout
        var iDetailsContainer: FrameLayout
        //var frameId : Int = 0
//        private val iOperationIcon: ImageView
//        private val iOperationActionImage: ImageView
        val iOperation: TextView
//        private val iAmount: TextView
//        private val iTimestamp: TextView
//        private val iStatus: TextView

        //private val mUserActivityOperation: UserActivityOperation? = null

        init {
//            iContainer = iView.findViewById(R.id.operation_container) as LinearLayout
            iDetailsContainer = iView.findViewById(ru.jobni.jobni.R.id.container_reg_rv_item) as FrameLayout
            //frameId = View.generateViewId()
            //iDetailsContainer.id = frameId
//            iOperationIcon = iView.findViewById(R.id.ledgerOperationIcon) as ImageView
//            iOperationActionImage = iView.findViewById(R.id.ledgerAction) as ImageView
            iOperation = iView.findViewById(R.id.tv_header_reg) as TextView
//            iAmount = iView.findViewById(R.id.ledgerOperationCurrencyAmount) as TextView
//            iTimestamp = iView.findViewById(R.id.ledgerOperationTimestamp) as TextView
//            iStatus = iView.findViewById(R.id.ledgerOperationStatus) as TextView

            // This linear layout status is GONE in order to avoid the view to use space
            // even when it is not seen, when any element selected the Adapter will manage the
            // behavior for showing the layout - container
            iDetailsContainer.visibility = View.GONE
        }
    }
}
