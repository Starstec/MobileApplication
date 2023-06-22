package com.github.user.soilitouraplication.ui.faq.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.user.soilitouraplication.R
import com.github.user.soilitouraplication.databinding.ItemFaqTitleBinding

class FaqAdapter : RecyclerView.Adapter<FaqAdapter.ParentViewHolder>() {
    
    private val items: ArrayList<SectionItem> = arrayListOf()
    
    fun addSectionItem(sectionItem: SectionItem) {
        items.add(sectionItem)
        notifyItemChanged(items.lastIndex)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemFaqTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding).apply {
            with(binding.root) {
                setOnClickListener {
                    val position =
                        adapterPosition.takeIf { position -> position != RecyclerView.NO_POSITION }
                            ?: return@setOnClickListener
                    val item = items[position]
                    item.expanded = !item.expanded
                    notifyItemChanged(position)
                }
                parentLayoutResource = R.layout.item_faq_parent
                secondLayoutResource = R.layout.item_faq_child
                
                duration = 200L
    
//                secondLayout.findViewById<TextView>(R.id.row_title).text = "Lorem"
            }
        }
    }
    
    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val sectionItem = items[position]
        with(holder.binding.expandableLayout) {
            if (sectionItem.expanded) {
                expand()
            } else {
                collapse()
            }
            parentLayout.findViewById<TextView>(R.id.title).text = sectionItem.title
            parentLayout.setBackgroundColor(ContextCompat.getColor(context, sectionItem.color))
    
            secondLayout.findViewById<TextView>(R.id.child_title).text = sectionItem.child
        }
    }
    
    override fun getItemCount() = items.size
    
    class ParentViewHolder(val binding: ItemFaqTitleBinding) :
        RecyclerView.ViewHolder(binding.root)
}