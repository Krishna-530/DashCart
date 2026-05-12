package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemBannerBinding

/**
 * Data class representing a promotional banner on the home screen.
 */
data class BannerItem(
    val badge: String,
    val title: String,
    val subtitle: String,
    val cta: String,
    val emoji: String,
    val backgroundResId: Int
)

/**
 * Adapter for the auto-scrolling promotional banner carousel (ViewPager2).
 */
class BannerAdapter(
    private val banners: List<BannerItem>
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(
        val binding: ItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val banner = banners[position]
        holder.binding.apply {
            tvBannerBadge.text = banner.badge
            tvBannerTitle.text = banner.title
            tvBannerSubtitle.text = banner.subtitle
            tvBannerCta.text = banner.cta
            tvBannerEmoji.text = banner.emoji
            bannerCard.setBackgroundResource(banner.backgroundResId)
        }
    }

    override fun getItemCount() = banners.size
}
