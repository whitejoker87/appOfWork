package ru.jobni.jobni.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.RecyclerView
import ru.jobni.jobni.R
import ru.jobni.jobni.databinding.RegContactsRecyclerItemBinding
import ru.jobni.jobni.viewmodel.RegViewModel

class RegContactsRecyclerAdapter(private val context: Context): RecyclerView.Adapter<RegContactsRecyclerAdapter.ContactHolder>() {

    private val viewModel: RegViewModel by lazy {
        ViewModelProviders.of(context as FragmentActivity).get(RegViewModel::class.java)
    }

    var contacts: MutableList<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding:RegContactsRecyclerItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.reg_contacts_recycler_item, parent, false)
        return ContactHolder(binding,viewModel)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ContactHolder(val binding: RegContactsRecyclerItemBinding, val viewModel: RegViewModel) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contactPos: Int) {
            binding.position = contactPos
            binding.viewmodel = viewModel
            binding.executePendingBindings()
        }
    }
}