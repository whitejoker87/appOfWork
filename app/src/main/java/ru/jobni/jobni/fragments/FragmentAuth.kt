package ru.jobni.jobni.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.jobni.jobni.R

class FragmentAuth : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.c_authorization, container, false)

        val email = view.findViewById<ImageButton>(R.id.btn_mail_ru)
        email.setOnClickListener {
            Toast.makeText(context, "onItemClick!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
