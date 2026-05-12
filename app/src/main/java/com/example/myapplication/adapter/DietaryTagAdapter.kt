package com.example.myapplication.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCategoryBinding

/**
 * Adapter for dietary filters (Vegan, Keto, Organic, etc.)
 */
class DietaryTagAdapter(
    private val tags: List<String>,
    private val onTagSelected: (String) -> Unit
) : RecyclerView.Adapter<DietaryTagAdapter.TagViewHolder>() {

    private var selectedPosition = 0

    inner class TagViewHolder(val binding: ItemCategoryBinding) : 
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.binding.tvCategoryLabel.text = tag
        holder.binding.tvCategoryEmoji.visibility = android.view.View.GONE
        
        val isSelected = position == selectedPosition
        
        if (isSelected) {
            holder.binding.tvCategory.setBackgroundResource(R.drawable.bg_category_selected)
            holder.binding.tvCategoryLabel.setTextColor(Color.WHITE)
        } else {
            holder.binding.tvCategory.setBackgroundResource(R.drawable.bg_category_unselected)
            holder.binding.tvCategoryLabel.setTextColor(Color.parseColor("#757575"))
        }

        holder.binding.tvCategory.setOnClickListener {
            val prev = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(prev)
            notifyItemChanged(selectedPosition)
            onTagSelected(tag)
        }
    }

    override fun getItemCount() = tags.size
}
