package com.netjdev.tfg_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.Pago
import com.netjdev.tfg_android_app.util.Utilities
import com.netjdev.tfg_android_app.vistas.ListOfPaymentsActivity
import kotlinx.android.synthetic.main.item_group_class.view.*
import kotlinx.android.synthetic.main.item_payment.view.*
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*

class PaymentAdapter(val paymentClick: (Pago) -> Unit) :
    RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    // Lista que almacena los documentos de una categoria
    var payments: List<Pago> = emptyList()

    //
    fun setData(list: List<Pago>) {
        payments = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentAdapter.PaymentViewHolder {
        return PaymentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val cuota = Calendar.getInstance()
        cuota.time = payments[position].cuotaPagada!!
        val mes = Utilities.getMonthName(cuota)
        // Obtener identificador del mes
        val identificador =


            //parent.resources.getIdentifier(mes.lowercase(), "string", this.packageName)

        val cuota = payments[position].cuotaPagada.
        holder.itemView.txtPayName.text = payments[position].cuotaPagada.toString()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}