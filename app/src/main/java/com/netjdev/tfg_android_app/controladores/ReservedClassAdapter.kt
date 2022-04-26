package com.netjdev.tfg_android_app.controladores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.UserClass
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.item_user_class.view.*

class ReservedClassAdapter(val reservedClassClick: (UserClass) -> Unit) :
    RecyclerView.Adapter<ReservedClassAdapter.ReservedClassViewHolder>() {

    // Lista que almacena los documentos de una categoria
    var reservedClasses: List<UserClass> = emptyList()

    //
    fun setData(list: List<UserClass>) {
        reservedClasses = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservedClassAdapter.ReservedClassViewHolder {
        return ReservedClassViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_class, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ReservedClassAdapter.ReservedClassViewHolder, position: Int
    ) {
        holder.itemView.txtClassName.text = reservedClasses[position].className
        val time = reservedClasses[position].day + " - " + reservedClasses[position].time
        holder.itemView.txtClassTime.text = time

        holder.itemView.btnDeleteClass.setOnClickListener {
            reservedClassClick(reservedClasses[position])
        }
    }

    override fun getItemCount(): Int {
        return reservedClasses.count()
    }

    class ReservedClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}