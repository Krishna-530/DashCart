package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemSidebarCategoryBinding
import com.example.myapplication.utils.DummyData

class SidebarCategoryAdapter(
    private val categories: List<String>,
    private var selectedCategory: String = "All",
    private val onCategorySelected: (String) -> Unit
) : RecyclerView.Adapter<SidebarCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSidebarCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSidebarCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = categories[holder.adapterPosition]
        with(holder.binding) {
            tvCategoryEmoji.text        = DummyData.getCategoryEmoji(cat)
            tvSidebarCategoryName.text  = cat

            val isSelected = cat == selectedCategory
            vSelectedIndicator.visibility = if (isSelected) View.VISIBLE else View.GONE
            tvSidebarCategoryName.setTypeface(
                null,
                if (isSelected) android.graphics.Typeface.BOLD
                else android.graphics.Typeface.NORMAL
            )

            rootSidebarCategory.setOnClickListener {
                val prev = selectedCategory
                selectedCategory = cat
                notifyItemChanged(categories.indexOf(prev))
                notifyItemChanged(holder.adapterPosition)
                onCategorySelected(cat)
            }
        }
    }
}
