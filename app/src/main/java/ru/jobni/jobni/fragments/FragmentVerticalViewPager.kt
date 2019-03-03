package ru.jobni.jobni.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.jobni.jobni.R

class FragmentVerticalViewPager : Fragment() {

    private var mNum: Int = 0
    private var mColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNum = if (arguments != null) arguments!!.getInt(MY_NUM_KEY) else 0
        mColor = if (arguments != null) arguments!!.getInt(MY_COLOR_KEY) else Color.BLACK
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.c_registration_01_mail, container, false)
        v.setBackgroundColor(mColor)
        val textView:TextView = v.findViewById(R.id.referral_message)
        textView.setText("Page $mNum")
        return v
    }

    companion object {

        private val MY_NUM_KEY = "num"
        private val MY_COLOR_KEY = "color"

        // You can modify the parameters to pass in whatever you want
        internal fun newInstance(num: Int, color: Int): FragmentVerticalViewPager {
            val f = FragmentVerticalViewPager()
            val args = Bundle()
            args.putInt(MY_NUM_KEY, num)
            args.putInt(MY_COLOR_KEY, color)
            f.setArguments(args)
            return f
        }
    }
}
