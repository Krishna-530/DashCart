package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCategoryBinding
import com.example.myapplication.utils.DummyData

/**
 * CategoryAdapter — horizontal chip strip on the Home screen.
 *
 * Each chip shows [emoji] [Category Name].
 * Selected chip → green fill + white text.
 * Unselected → white background + grey border + green text.
 *
 * Tapping a chip calls [onCategorySelected] so HomeActivity can
 * hide/show sections and update the ViewModel filter.
 */
class CategoryAdapter(
    private val categories: List<String>,
    private val onCategorySelected: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[holder.adapterPosition]
        val ctx = holder.itemView.context

        // Set emoji and label
        holder.binding.tvCategoryEmoji.text = DummyData.getCategoryEmoji(category)
        holder.binding.tvCategoryLabel.text = category

        // Apply selected / unselected style
        val isSelected = (holder.adapterPosition == selectedPosition)
        if (isSelected) {
            holder.binding.tvCategory.setBackgroundResource(R.drawable.bg_category_selected)
            holder.binding.tvCategoryLabel.setTextColor(ctx.getColor(R.color.white))
        } else {
            holder.binding.tvCategory.setBackgroundResource(R.drawable.bg_category_unselected)
            holder.binding.tvCategoryLabel.setTextColor(ctx.getColor(R.color.colorPrimary))
        }

        // Click on the whole pill row
        holder.binding.tvCategory.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onCategorySelected(category)
        }
    }

    override fun getItemCount(): Int = categories.size
}
