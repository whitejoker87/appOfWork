package ru.jobni.jobni.fragments

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.jobni.jobni.R;
import kotlinx.android.synthetic.main.fragment_profile_bottom_dialog.*
import kotlinx.android.synthetic.main.fragment_profile_bottom_dialog_item.view.*

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ProfileBottomDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [ProfileBottomDialogFragment.Listener].
 */
class ProfileBottomDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_bottom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = ItemAdapter(arguments!!.getStringArray(LIST_MENU))
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            mListener = parent as Listener
        } else {
            mListener = context as Listener
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onItemClicked(position: Int)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_profile_bottom_dialog_item, parent, false)) {

        internal val text: TextView = itemView.text

        init {
            text.setOnClickListener {
                mListener?.let {
                    it.onItemClicked(adapterPosition)
                    dismiss()
                }
            }
        }
    }

    private inner class ItemAdapter internal constructor(private val listMenu: kotlin.Array<String>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = listMenu[position]
        }

        override fun getItemCount(): Int {
            return listMenu.size
        }
    }

    companion object {

        fun newInstance(isLogged: Boolean): ProfileBottomDialogFragment =
            ProfileBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    if (isLogged) putStringArray(LIST_MENU, listMeniuLogged)
                    else putStringArray(LIST_MENU, listMenuNotLogged)
                }
            }

        private const val LIST_MENU = "list_menu"

        private val listMenuNotLogged = arrayOf("Авторизация", "Регистрация", "Восстановление пароля")
        private val listMeniuLogged = arrayOf("ID:", "REF:", "Реферальная программа", "Cashback", "Учетная запись", "Выход")
    }
}
