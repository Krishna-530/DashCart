package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCategoryGridBinding
import com.example.myapplication.utils.DummyData

/**
 * Data class for category grid items shown on the home screen.
 */
data class CategoryGridItem(
    val name: String,
    val emoji: String,
    val itemCount: Int
)

/**
 * Adapter for the "Shop by Category" grid on the home screen.
 * Shows category cards with emoji icons and item counts.
 */
class CategoryGridAdapter(
    private val categories: List<CategoryGridItem>,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryGridAdapter.CategoryGridViewHolder>() {

    inner class CategoryGridViewHolder(
        val binding: ItemCategoryGridBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryGridViewHolder {
        val binding = ItemCategoryGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryGridViewHolder, position: Int) {
        val item = categories[position]
        holder.binding.apply {
            tvCategoryEmoji.text = item.emoji
            tvCategoryName.text = item.name
            tvCategoryCount.text = "${item.itemCount} items"
            root.setOnClickListener { onCategoryClick(item.name) }
        }
    }

    override fun getItemCount() = categories.size
}
