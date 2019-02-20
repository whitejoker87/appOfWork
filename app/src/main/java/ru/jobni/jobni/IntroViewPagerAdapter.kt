package ru.jobni.jobni

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import ru.jobni.jobni.databinding.IntroSlideBinding
import ru.jobni.jobni.model.IntroSlide

class IntroViewPagerAdapter(
    context: Context?,
     var slides: List<IntroSlide>?,
    onclickBtnSlide: IOnclickBtnSlide
) : PagerAdapter() {

    //private lateinit var slides: List<IntroSlide>
    private lateinit var onclickBtnSlide: IOnclickBtnSlide

    init {
        //this.slides = slides
        this.onclickBtnSlide = onclickBtnSlide
    }



    private lateinit var ivSlide: ImageView
//    private lateinit var tvTitleSlide: TextView
//    private lateinit var tvDescrSlide: TextView
//    private lateinit var btnSlide: Button
//
//    var LOG_TAG = "my_log"

//    fun TrainingViewPagerAdapter(
//        context: Context,
//        slides: List<IntroSlide>,
//        onclickBtnSlide: IOnclickBtnSlide
//    ) {
//        //this.context = context;
//        this.slides = slides
//        this.onclickBtnSlide = onclickBtnSlide
//    }

    @NonNull
    override fun instantiateItem(@NonNull collection: ViewGroup, position: Int): Any {

        val binding =
            IntroSlideBinding.inflate(LayoutInflater.from(collection.context), collection, false)

        ivSlide = binding.ivSlide
//        tvTitleSlide = binding.tvSlideTitle
//        tvDescrSlide = binding.tvSlideDescr
//        btnSlide = binding.btnSlide

        Picasso.get()
            .load(slides!![position].ivIntroID)
            .fit()
            .into(ivSlide)

//        tvTitleSlide.setText(slides[position].getTvTitleTraining())
//        tvTitleSlide.setText(slides[position].getTvDescrTraining())
//        tvTitleSlide.setText(slides[position].getBtnTraining())

//        btnSlide.setOnClickListener {
//            Log.d(LOG_TAG, "Register button clicked.")
//            onclickBtnSlide.onClickButtonCallback(position)
//        }

        collection.addView(binding.getRoot())
        return binding.getRoot()
    }

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull view: Any) {
        container.removeView(view as View)
    }

    override fun getCount(): Int {
        if (slides == null) return 0
        return slides!!.size
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull `object`: Any): Boolean =view === `object`

}