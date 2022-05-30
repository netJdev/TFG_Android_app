package com.netjdev.tfg_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.Pago
import com.netjdev.tfg_android_app.util.Utilities
import kotlinx.android.synthetic.main.item_payment.view.*
import java.util.*

class PaymentAdapter(val paymentClick: (Pago) -> Unit) :
    RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    // Lista que almacena los documentos de una categoria
    var payments: List<Pago> = emptyList()

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
        val context = holder.itemView.context
        val pago = Calendar.getInstance()
        pago.time = payments[position].cuotaPagada!!
        val mes = Utilities.getMonthName(pago)
        // Obtener identificador del mes
        val identificador = context.resources.getIdentifier(mes.lowercase(), "string", context.packageName)

        val cuota = "${context.getString(identificador)} - ${pago.get(Calendar.YEAR)}"
        holder.itemView.txtPayName.text = cuota
        holder.itemView.txtPayStatus.text = context.getString(R.string.pay_status)
    }

    override fun getItemCount(): Int {
        return payments.count()
    }

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}