    package ru.jobni.jobni.utils

    import android.content.Context
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.view.animation.Animation
    import android.view.animation.AnimationUtils
    import android.widget.FrameLayout
    import android.widget.TextView
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.FragmentActivity
    import androidx.lifecycle.LifecycleOwner
    import androidx.lifecycle.Observer
    import androidx.lifecycle.ViewModelProviders
    import androidx.recyclerview.widget.RecyclerView
    import ru.jobni.jobni.MainActivity
    import ru.jobni.jobni.R
    import ru.jobni.jobni.fragments.FragmentRegOne
    import ru.jobni.jobni.fragments.FragmentRegThree
    import ru.jobni.jobni.fragments.FragmentRegTwo
    import ru.jobni.jobni.viewmodel.MainViewModel
    import ru.jobni.jobni.viewmodel.RegViewModel


    class RegRecyclerAdapter(/*private val mObjects: List<String>,*/ private val mContext: Context) : RecyclerView.Adapter<RegRecyclerAdapter.ViewHolder>() {

        private val viewModel: MainViewModel by lazy {
            ViewModelProviders.of(mContext as FragmentActivity).get(MainViewModel::class.java)
        }

        private val regViewModel: RegViewModel by lazy {
            ViewModelProviders.of(mContext as FragmentActivity).get(RegViewModel::class.java)
        }

        val fragmentManager = (mContext as MainActivity).supportFragmentManager

        private val mAnimationUp: Animation = AnimationUtils.loadAnimation(mContext, ru.jobni.jobni.R.anim.slide_up)
        private val mAnimationDown: Animation = AnimationUtils.loadAnimation(mContext, ru.jobni.jobni.R.anim.slide_down)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(ru.jobni.jobni.R.layout.reg_recycler_item, parent, false)
            return ViewHolder(view, regViewModel)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Get fragment manager inside our fragment

            if (position == viewModel.getNumberOfVisibleItemReg().value){
                holder.iDetailsContainer.visibility = View.VISIBLE
                holder.iOperation.setTextColor(mContext.resources.getColor(ru.jobni.jobni.R.color.white))
            }
            else {
                holder.iDetailsContainer.visibility = View.GONE
                holder.iOperation.setTextColor(mContext.resources.getColor(ru.jobni.jobni.R.color.black))
            }
            when (position) {
                0 -> {
                    holder.iOperation.text = "1.Регистрационные данные"
                    //holder.iOperation.setTextColor(mContext.resources.getColor(ru.jobni.jobni.R.color.white))
                    //holder.iDetailsContainer.visibility = View.VISIBLE
                    //fragmentManager.beginTransaction().replace(ru.jobni.jobni.R.id.container_reg_rv_item, FragmentRegOne()).commit()
                    //if (fragmentManager.backStackEntryCount == 0) setFragmentInItem(holder, position)
                }
                1 -> holder.iOperation.text = "2.Основная информация"
                2 -> holder.iOperation.text = "3.Контактные данные"
            }


            // Remember that RecyclerView does not have onClickListener, you should implement it
            holder.itemView.setOnClickListener(object : View.OnClickListener {

                // Method that could us an unique id
                val uniqueId = View.generateViewId()

                override fun onClick(view: View) {
                    // Hide details
                    // iDetailsContainer object could be checked on inner class ViewHolder
                    if (holder.iDetailsContainer.isShown) {
                        holder.iDetailsContainer.visibility = View.GONE
                        holder.iOperation.setTextColor(mContext.resources.getColor(ru.jobni.jobni.R.color.black))
                    } else {
                        setFragmentInItem(holder, position)
                        notifyDataSetChanged()
                    }
                }
            })
        }

        override fun getItemCount(): Int {
            return 3
        }

        fun setFragmentInItem(holder: ViewHolder, position: Int) {
            viewModel.setNumberOfVisibleItemReg(position)
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
            val newContainerId = position + 1
            // Set the new Id to our know fragment container
            holder.iDetailsContainer.id = newContainerId

            // Just for Testing we are going to create a new fragment according
            // if the view position is pair one fragment type is created, if not
            // a different one is used
            val f: Fragment
            when (position) {
                0 -> f = FragmentRegOne()
                1 -> f = FragmentRegTwo()
                2 -> f = FragmentRegThree()
                else -> f = FragmentRegOne()
            }

            // Then just replace the recycler view fragment as usually
            fragmentManager.beginTransaction().replace(newContainerId, f).commit()

            // Once all fragment replacement is done we can show the hidden container
            //holder.iDetailsContainer.visibility = View.VISIBLE
        }

        class ViewHolder(private val iView: View, private val viewModel: RegViewModel) : RecyclerView.ViewHolder(iView) {
            var iDetailsContainer: FrameLayout = iView.findViewById(ru.jobni.jobni.R.id.container_reg_rv_item) as FrameLayout
            val iOperation: TextView = iView.findViewById(ru.jobni.jobni.R.id.tv_header_reg) as TextView

            init {
//                this.iOperation.setOnClickListener(View.OnClickListener {
//                    viewModel.setNumberOfVisibleItemReg(adapterPosition)
//                    notifyDataSetChanged()
//                })

                // This linear layout status is GONE in order to avoid the view to use space
                // even when it is not seen, when any element selected the Adapter will manage the
                // behavior for showing the layout - container
                //iDetailsContainer.visibility = View.GONE
            }

        }
    }
