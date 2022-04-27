package com.netjdev.tfg_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.GroupClass
import kotlinx.android.synthetic.main.item_group_class.view.*

class GroupClassAdapter(val groupClassesClick: (GroupClass) -> Unit) :
    RecyclerView.Adapter<GroupClassAdapter.GroupClassViewHolder>() {

    // Lista que almacena los documentos de una categoria
    var groupClasses: List<GroupClass> = emptyList()

    //
    fun setData(list: List<GroupClass>) {
        groupClasses = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupClassViewHolder {
        return GroupClassViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_class, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupClassAdapter.GroupClassViewHolder, position: Int) {
        holder.itemView.groupClassNameText.text = groupClasses[position].name
        holder.itemView.setOnClickListener {
            groupClassesClick(groupClasses[position])
        }
    }

    override fun getItemCount(): Int {
        return groupClasses.count()
    }

    class GroupClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}