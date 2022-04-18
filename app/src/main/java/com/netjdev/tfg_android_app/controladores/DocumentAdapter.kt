package com.netjdev.tfg_android_app.controladores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.Document
import kotlinx.android.synthetic.main.item_document.view.*

class DocumentAdapter(val documentClick: (Document) -> Unit) :
    RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    // Lista que almacena los documentos de una categoria
    var documents: List<Document> = emptyList()

    //
    fun setData(list: List<Document>){
        documents = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DocumentAdapter.DocumentViewHolder {
        return DocumentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DocumentAdapter.DocumentViewHolder, position: Int) {
        holder.itemView.documentNameText.text = documents[position].name
        holder.itemView.setOnClickListener {
            documentClick(documents[position])
        }
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}