package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.BottomSheetProductBinding
import com.example.myapplication.model.Product
import com.example.myapplication.utils.CartManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt

/**
 * ProductBottomSheetFragment — slides up from the bottom when a product is tapped.
 *
 * Design: full-width product photo → floating card overlaps by 24dp (rounded top corners)
 *         → description, delivery badges, qty stepper, Add to Cart.
 */
class ProductBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetProductBinding? = null
    private val binding get() = _binding!!

    private var quantity = 1
    private lateinit var product: Product

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"

        fun newInstance(product: Product): ProductBottomSheetFragment {
            return ProductBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PRODUCT_ID, product.id)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productId = arguments?.getInt(ARG_PRODUCT_ID) ?: run { dismiss(); return }
        val found = com.example.myapplication.utils.DummyData.getById(productId)
            ?: run { dismiss(); return }
        product = found

        // ── Load image ─────────────────────────────────────────────────────
        Glide.with(this)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_grocery_logo)
            .error(R.drawable.ic_grocery_logo)
            .into(binding.imgSheet)

        // ── Fill data ──────────────────────────────────────────────────────
        binding.tvSheetCompany.text     = product.company
        binding.tvSheetName.text        = product.name
        binding.tvSheetPrice.text       = "₹${product.price.roundToInt()}"
        binding.tvSheetDescription.text = product.description.ifEmpty {
            "A quality product by ${product.company}. Sourced fresh and delivered right to your door."
        }

        // Fake rating (product id used as a seed for slight variation)
        val rating = 3.8 + (product.id % 5) * 0.1
        binding.tvSheetRating.text = "★ ${String.format("%.1f", rating)}"

        // ── Out of Stock ───────────────────────────────────────────────────
        if (product.isOutOfStock) {
            binding.tvSheetOutOfStock.visibility = View.VISIBLE
            binding.btnSheetAdd.isEnabled = false
            binding.btnSheetAdd.text = "Out of Stock"
            binding.btnSheetAdd.alpha = 0.5f
            binding.btnSheetDecrease.isEnabled = false
            binding.btnSheetIncrease.isEnabled = false
        }

        // ── Quantity Stepper ───────────────────────────────────────────────
        updateBottomBar()

        binding.btnSheetIncrease.setOnClickListener {
            quantity++
            updateBottomBar()
        }
        binding.btnSheetDecrease.setOnClickListener {
            if (quantity > 1) { quantity--; updateBottomBar() }
        }

        // ── Add to Cart ────────────────────────────────────────────────────
        binding.btnSheetAdd.setOnClickListener {
            repeat(quantity) { CartManager.addToCart(product) }
            Toast.makeText(
                requireContext(),
                "✅ Added ${product.name} ×$quantity",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
    }

    private fun updateBottomBar() {
        binding.tvSheetQty.text = quantity.toString()
        val total = (product.price * quantity).roundToInt()
        if (!product.isOutOfStock) {
            binding.btnSheetAdd.text = "Add $quantity to Cart  ·  ₹$total"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
