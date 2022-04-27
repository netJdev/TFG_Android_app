package com.netjdev.tfg_android_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.modelos.DocCategory
import kotlinx.android.synthetic.main.item_chat.view.*

class DocCategoryAdapter(val docCategoryClick: (DocCategory) -> Unit) :
    RecyclerView.Adapter<DocCategoryAdapter.DocCategoryViewHolder>() {

    // Lista que almacena las categorias de documentos (nombres de las carpetas)
    var docCategories: List<DocCategory> = emptyList()

    //
    fun setData(list: List<DocCategory>){
        docCategories = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocCategoryAdapter.DocCategoryViewHolder {
        return DocCategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_doc_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DocCategoryViewHolder, position: Int) {
        holder.itemView.docTypeNameText.text = docCategories[position].name
        holder.itemView.setOnClickListener {
            docCategoryClick(docCategories[position])
        }
    }

    override fun getItemCount(): Int {
        return docCategories.size
    }

    class DocCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}