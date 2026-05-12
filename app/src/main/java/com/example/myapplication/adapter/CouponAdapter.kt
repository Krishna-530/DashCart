package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemCategoryBinding
import android.graphics.Color

/**
 * Simple adapter to show available coupon codes as horizontal chips.
 */
class CouponAdapter(
    private val coupons: List<String>,
    private val onCouponClick: (String) -> Unit
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    inner class CouponViewHolder(val binding: ItemCategoryBinding) : 
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CouponViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val code = coupons[position]
        holder.binding.tvCategoryLabel.text = code
        holder.binding.tvCategoryEmoji.text = "🎟️"
        
        // Use a slight highlight for coupons
        holder.binding.tvCategory.setBackgroundResource(com.example.myapplication.R.drawable.bg_category_unselected)
        holder.binding.tvCategoryLabel.setTextColor(Color.parseColor("#0C831F"))

        holder.binding.tvCategory.setOnClickListener {
            onCouponClick(code)
        }
    }

    override fun getItemCount() = coupons.size
}
