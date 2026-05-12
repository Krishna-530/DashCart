package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.adapter.BannerAdapter
import com.example.myapplication.adapter.BannerItem
import com.example.myapplication.adapter.CategoryAdapter
import com.example.myapplication.adapter.CategoryGridAdapter
import com.example.myapplication.adapter.CategoryGridItem
import com.example.myapplication.adapter.HorizontalProductAdapter
import com.example.myapplication.adapter.OfferCardAdapter
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.adapter.SidebarCategoryAdapter
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.model.Product
import com.example.myapplication.ui.ProductBottomSheetFragment
import com.example.myapplication.utils.CartManager
import com.example.myapplication.utils.DummyData
import com.example.myapplication.viewmodel.HomeViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val notificationViewModel: com.example.myapplication.viewmodel.NotificationViewModel by viewModels()

    // ── Grid adapter (2-column, for filtered products only) ──────────────
    private lateinit var productAdapter: ProductAdapter

    // ── Horizontal swipe adapters (Swiggy-style cards) ───────────────────
    private lateinit var quickAddAdapter: HorizontalProductAdapter
    private lateinit var mostShoppedAdapter: HorizontalProductAdapter
    private lateinit var newlyAddedAdapter: HorizontalProductAdapter
    private lateinit var highestDiscountAdapter: HorizontalProductAdapter
    private lateinit var buyItAgainAdapter: HorizontalProductAdapter

    // ── Promotional adapters ─────────────────────────────────────────────
    private lateinit var offerCardAdapter: OfferCardAdapter

    // ── Navigation ───────────────────────────────────────────────────────
    private lateinit var sidebarAdapter: SidebarCategoryAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle

    // ── Banner auto-scroll ───────────────────────────────────────────────
    private val bannerHandler = Handler(Looper.getMainLooper())
    private var bannerRunnable: Runnable? = null
    private var currentBannerPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDrawer()
        setupDrawer()
        setupBannerCarousel()
        setupCategoryGrid()
        setupRecyclerViews()
        setupSearchBar()
        observeViewModel()
        loadOrderHistoryForPredictiveShopping()

        // ── Notifications Setup ──
        com.example.myapplication.utils.NotificationHelper.createChannels(this)
        requestNotificationPermission()
        checkAndShowWelcomeOffer()

        simulateLoading()
    }

    /**
     * Feature: Request POST_NOTIFICATIONS for Android 13+
     */
    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != 
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    /**
     * Simulates a network delay to demonstrate the professional shimmer effect.
     */
    companion object {
        var isPreloaded = false // Splash sets this to true to skip double loading
    }

    private fun simulateLoading() {
        if (isPreloaded) {
            // Data is already "pre-loaded" from splash, skip shimmer delay
            binding.shimmerViewContainer.visibility = View.GONE
            binding.llHomeContent.visibility = View.VISIBLE
            isPreloaded = false // Reset for future visits
            return
        }

        binding.shimmerViewContainer.startShimmer()
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.llHomeContent.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            binding.llHomeContent.visibility = View.VISIBLE
        }, 2000) // 2 second delay for realistic feel
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
        checkActiveOrder()
        startBannerAutoScroll() // Resume carousel when user comes back
    }

    override fun onPause() {
        super.onPause()
        stopBannerAutoScroll()
    }

    // ── Toolbar ────────────────────────────────────────────────────────────
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.ivCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.flNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        // Floating Cart Strip click
        binding.layoutCartStrip.cardCartStrip.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Add a gentle floating animation
        binding.btnTrackOrder.setOnClickListener {
            startActivity(Intent(this, OrderTrackingActivity::class.java))
        }

        updateCartBadge()
        checkActiveOrder()
    }

    private fun checkActiveOrder() {
            val activeOrderId = com.example.myapplication.utils.SharedPrefsHelper.getActiveOrderId(this)
        if (activeOrderId != null) {
            binding.btnTrackOrder.visibility = View.VISIBLE
        } else {
            binding.btnTrackOrder.visibility = View.GONE
        }
    }

    // ── Sidebar Drawer ─────────────────────────────────────────────────────
    private fun setupDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            android.R.string.ok, android.R.string.cancel
        )
        drawerToggle.drawerArrowDrawable.color = getColor(android.R.color.white)
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // "View All" button opens the drawer
        binding.btnOpenCategories.setOnClickListener {
            binding.drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
        }

        val categories = DummyData.getCategories()
        sidebarAdapter = SidebarCategoryAdapter(categories, "All") { selectedCategory ->
            binding.drawerLayout.closeDrawers()
            applyCategory(selectedCategory)
        }
        binding.rvSidebarCategories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = sidebarAdapter
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BANNER CAROUSEL — auto-scrolling promo banners
    // ══════════════════════════════════════════════════════════════════════
    private fun setupBannerCarousel() {
        val banners = listOf(
            BannerItem(
                badge = "LIMITED TIME",
                title = "Fresh Deals\nUp To 60% Off",
                subtitle = "On fruits, veggies & daily essentials",
                cta = "Shop Now →",
                emoji = "🥬",
                backgroundResId = R.drawable.bg_banner_1
            ),
            BannerItem(
                badge = "FREE DELIVERY",
                title = "Free Delivery\nOn Orders ₹199+",
                subtitle = "No minimum order for first purchase",
                cta = "Order Now →",
                emoji = "🚚",
                backgroundResId = R.drawable.bg_banner_2
            ),
            BannerItem(
                badge = "WEEKEND SALE",
                title = "Weekend Special\nBuy 2 Get 1 Free",
                subtitle = "On snacks, beverages & bakery items",
                cta = "Grab Deals →",
                emoji = "🎉",
                backgroundResId = R.drawable.bg_banner_3
            ),
            BannerItem(
                badge = "⚡ FLASH SALE",
                title = "Flash Sale\nEnds Tonight!",
                subtitle = "Up to 70% off on cleaning & personal care",
                cta = "Hurry Up →",
                emoji = "⏰",
                backgroundResId = R.drawable.bg_banner_4
            )
        )

        val bannerAdapter = BannerAdapter(banners)
        binding.vpBanners.adapter = bannerAdapter

        // Setup dot indicators
        setupDotIndicators(banners.size)

        binding.vpBanners.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentBannerPage = position
                updateDotIndicators(position)
            }
        })
    }

    private fun setupDotIndicators(count: Int) {
        binding.llDotIndicators.removeAllViews()
        for (i in 0 until count) {
            val dot = ImageView(this).apply {
                setImageResource(
                    if (i == 0) R.drawable.bg_dot_indicator_active
                    else R.drawable.bg_dot_indicator_inactive
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginStart = 4
                    marginEnd = 4
                }
                layoutParams = params
            }
            binding.llDotIndicators.addView(dot)
        }
    }

    private fun updateDotIndicators(selectedPosition: Int) {
        for (i in 0 until binding.llDotIndicators.childCount) {
            val dot = binding.llDotIndicators.getChildAt(i) as ImageView
            dot.setImageResource(
                if (i == selectedPosition) R.drawable.bg_dot_indicator_active
                else R.drawable.bg_dot_indicator_inactive
            )
        }
    }

    private fun startBannerAutoScroll() {
        bannerRunnable = object : Runnable {
            override fun run() {
                val adapter = binding.vpBanners.adapter ?: return
                currentBannerPage = (currentBannerPage + 1) % adapter.itemCount
                binding.vpBanners.setCurrentItem(currentBannerPage, true)
                bannerHandler.postDelayed(this, 4000)
            }
        }
        bannerHandler.postDelayed(bannerRunnable!!, 4000)
    }

    private fun stopBannerAutoScroll() {
        bannerRunnable?.let { bannerHandler.removeCallbacks(it) }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SHOP BY CATEGORY — 4-column grid with emoji icons
    // ══════════════════════════════════════════════════════════════════════
    private fun setupCategoryGrid() {
        val allProducts = DummyData.getProducts()
        val categories = DummyData.getCategories().filter { it != "All" }

        val categoryItems = categories.map { categoryName ->
            val count = allProducts.count { it.category == categoryName }
            val emoji = DummyData.getCategoryEmoji(categoryName)
            CategoryGridItem(categoryName, emoji, count)
        }

        val gridAdapter = CategoryGridAdapter(categoryItems) { categoryName ->
            applyCategory(categoryName)
        }

        binding.rvCategoryGrid.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 4)
            adapter = gridAdapter
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TODAY'S OFFERS — horizontal strip of discounted products
    // ══════════════════════════════════════════════════════════════════════
    private fun setupOffersSection(onAddToCart: (Product) -> Unit, onProductClick: (Product) -> Unit) {
        val allProducts = DummyData.getProducts()
        val discountedProducts = allProducts.filter {
            it.originalPrice != null && it.originalPrice > it.price
        }

        offerCardAdapter = OfferCardAdapter(discountedProducts, onAddToCart, onProductClick)

        binding.rvOffers.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = offerCardAdapter
        }
    }

    /**
     * Central category-change handler.
     * • When "All" is selected → show promo sections (banners, categories, offers, trust).
     * • When a specific category → hide promo sections, show filtered grid only.
     */
    private fun applyCategory(category: String) {
        val isAll = (category == "All")

        // Show/hide the curated home-feed sections
        val feedVisibility = if (isAll) View.VISIBLE else View.GONE
        binding.llMostShoppedBanner.visibility = feedVisibility
        binding.divMostShopped.visibility      = feedVisibility
        binding.llNewlyAddedSection.visibility  = feedVisibility
        binding.divNewlyAdded.visibility        = feedVisibility
        binding.llBestDealsSection.visibility   = feedVisibility
        binding.divBestDeals.visibility         = feedVisibility

        // Promotional sections
        binding.llBannerSection.visibility        = feedVisibility
        binding.divBanners.visibility             = feedVisibility
        binding.llCategoryGridSection.visibility   = feedVisibility
        binding.divCategoryGrid.visibility        = feedVisibility
        binding.llOffersSection.visibility         = feedVisibility
        binding.divOffers.visibility              = feedVisibility
        binding.llTrustSection.visibility          = feedVisibility
        binding.llQuickAddSection.visibility       = feedVisibility
        binding.divTrust.visibility               = feedVisibility

        // Filtered products grid (only when filtering by category)
        val filterVisibility = if (isAll) View.GONE else View.VISIBLE
        binding.llFilteredHeader.visibility = filterVisibility
        binding.rvProducts.visibility      = filterVisibility

        // Update the filtered header label
        binding.tvSelectedCategory.text = category

        // Banner auto-scroll
        if (isAll) startBannerAutoScroll() else stopBannerAutoScroll()

        // Push filter to ViewModel → triggers the grid LiveData observer
        viewModel.onCategorySelected(category)
    }



    // ── RecyclerViews ──────────────────────────────────────────────────────
    private fun setupRecyclerViews() {
        // Shared logic for refreshing UI after cart changes
        val onCartUpdated: (Product?) -> Unit = { _ ->
            updateCartBadge()
        }

        val onProductClick: (Product) -> Unit = { product ->
            ProductBottomSheetFragment
                .newInstance(product)
                .show(supportFragmentManager, "product_sheet")
        }

        // Setup the Today's Offers section with the shared callbacks
        setupOffersSection(onCartUpdated, onProductClick)

        // ── Quick Add (New) ───────────────────────────────────────────────
        val quickAddProducts = DummyData.getProducts().filter { 
            it.category == "Dairy" || it.category == "Bakery" 
        }.take(6)
        quickAddAdapter = HorizontalProductAdapter(quickAddProducts, onCartUpdated, onProductClick)
        binding.rvQuickAdd.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = quickAddAdapter
        }

        // ── 2-column grid for filtered products ───────────────────────────
        productAdapter = ProductAdapter(emptyList(), onCartUpdated, onProductClick)
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = productAdapter
        }

        // ── Horizontal swipe: Most Shopped ────────────────────────────────
        mostShoppedAdapter = HorizontalProductAdapter(emptyList(), onCartUpdated, onProductClick)
        binding.rvMostShopped.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = mostShoppedAdapter
        }

        // ── Horizontal swipe: Newly Added ─────────────────────────────────
        newlyAddedAdapter = HorizontalProductAdapter(emptyList(), onCartUpdated, onProductClick)
        binding.rvNewlyAdded.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = newlyAddedAdapter
        }

        // ── Horizontal swipe: Best Deals ──────────────────────────────────
        highestDiscountAdapter = HorizontalProductAdapter(emptyList(), onCartUpdated, onProductClick)
        binding.rvHighestDiscount.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = highestDiscountAdapter
        }

        // ── Feature 5: Buy It Again ──────────────────────────────────────
        buyItAgainAdapter = HorizontalProductAdapter(emptyList(), onCartUpdated, onProductClick)
        binding.rvBuyItAgain.apply {
            layoutManager = LinearLayoutManager(
                this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = buyItAgainAdapter
        }
    }

    // ── Search bar ────────────────────────────────────────────────────────
    private fun setupSearchBar() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchQueryChanged(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // ── Observers ─────────────────────────────────────────────────────────
    private fun observeViewModel() {

        // Grid (updated when filtering by category)
        viewModel.filteredProducts.observe(this) { products ->
            productAdapter.updateList(products)
        }

        // Most Shopped horizontal strip
        viewModel.mostShoppedProducts.observe(this) { products ->
            if (products.isEmpty()) {
                binding.llMostShoppedBanner.visibility = View.GONE
                binding.divMostShopped.visibility      = View.GONE
            } else {
                // Only show if we're on "All" (category might be selected)
                mostShoppedAdapter.updateList(products)
            }
        }

        // Newly Added horizontal strip
        viewModel.newlyAddedProducts.observe(this) { products ->
            if (products.isEmpty()) {
                binding.llNewlyAddedSection.visibility = View.GONE
                binding.divNewlyAdded.visibility       = View.GONE
            } else {
                newlyAddedAdapter.updateList(products)
            }
        }

        // Best Deals horizontal strip
        viewModel.highestDiscountProducts.observe(this) { products ->
            if (products.isEmpty()) {
                binding.llBestDealsSection.visibility = View.GONE
                binding.divBestDeals.visibility       = View.GONE
            } else {
                highestDiscountAdapter.updateList(products)
            }
        }

        // Feature 5: Buy It Again
        viewModel.buyItAgainProducts.observe(this) { products ->
            if (products.isEmpty()) {
                binding.llBuyItAgainSection.visibility = View.GONE
            } else {
                binding.llBuyItAgainSection.visibility = View.VISIBLE
                buyItAgainAdapter.updateList(products)
            }
        }

        // Unread Notifications
        notificationViewModel.allNotifications.observe(this) { notifications ->
            val unreadCount = notifications.count { !it.isRead }
            if (unreadCount > 0) {
                binding.tvNotificationBadge.visibility = View.VISIBLE
                binding.tvNotificationBadge.text = if (unreadCount > 9) "9+" else unreadCount.toString()
            } else {
                binding.tvNotificationBadge.visibility = View.GONE
            }
        }
    }

    /**
     * Logic to show a demo welcome offer if it hasn't been shown yet.
     */
    private fun checkAndShowWelcomeOffer() {
        val prefs = com.example.myapplication.utils.SharedPrefsHelper
        if (!prefs.hasSeenWelcomeOffer(this)) {
            notificationViewModel.addOfferNotification(
                "🎁 Welcome Gift!",
                "Get 50% OFF on your first order! Use code WELCOME50.",
                "welcome_promo"
            )
            prefs.setHasSeenWelcomeOffer(this, true)
        }
    }

    private var previousItemCount = 0

    // ── Cart badge & Floating Strip ───────────────────────────────────────
    private fun updateCartBadge() {
        val count = CartManager.getItemCount()
        val total = CartManager.getGrandTotal()

        if (count > 0) {
            // Update Toolbar Badge
            binding.tvCartBadge.visibility = View.VISIBLE
            binding.tvCartBadge.text = count.toString()
            
            // Update Floating Strip
            val isNewlyVisible = binding.layoutCartStrip.cardCartStrip.visibility == View.GONE
            binding.layoutCartStrip.cardCartStrip.visibility = View.VISIBLE
            binding.layoutCartStrip.tvStripItemCount.text = if (count == 1) "1 ITEM" else "$count ITEMS"
            binding.layoutCartStrip.tvStripTotal.text = "₹${total.toInt()}"

            // Entrance animation (Slide up and stop)
            if (isNewlyVisible) {
                binding.layoutCartStrip.cardCartStrip.post {
                    binding.layoutCartStrip.cardCartStrip.translationY = 1500f // Start much further down
                    binding.layoutCartStrip.cardCartStrip.animate()
                        .translationY(0f)
                        .setDuration(800)
                        .setInterpolator(android.view.animation.DecelerateInterpolator())
                        .start()
                }
            }

            // Play pop animation if count increased (but not for first time)
            if (count > previousItemCount && !isNewlyVisible) {
                val popAnim = AnimationUtils.loadAnimation(this, R.anim.badge_pop)
                binding.tvCartBadge.startAnimation(popAnim)
                binding.layoutCartStrip.cardCartStrip.startAnimation(popAnim)
            }
        } else {
            binding.tvCartBadge.visibility = View.GONE
            binding.layoutCartStrip.cardCartStrip.visibility = View.GONE
        }
        previousItemCount = count

        // Feature 5: Update Buy It Again products on start
        loadOrderHistoryForPredictiveShopping()

        // Notify all adapters
        if (::quickAddAdapter.isInitialized) quickAddAdapter.notifyDataSetChanged()
        if (::productAdapter.isInitialized) productAdapter.notifyDataSetChanged()
        if (::mostShoppedAdapter.isInitialized) mostShoppedAdapter.notifyDataSetChanged()
        if (::newlyAddedAdapter.isInitialized) newlyAddedAdapter.notifyDataSetChanged()
        if (::highestDiscountAdapter.isInitialized) highestDiscountAdapter.notifyDataSetChanged()
        if (::offerCardAdapter.isInitialized) offerCardAdapter.notifyDataSetChanged()
        if (::buyItAgainAdapter.isInitialized) buyItAgainAdapter.notifyDataSetChanged()
    }

    private fun loadOrderHistoryForPredictiveShopping() {
        lifecycleScope.launch {
            val db = com.example.myapplication.db.AppDatabase.getInstance(this@HomeActivity)
            val orders = db.orderDao().getAllOrders() 
            
            // Extract product names from order summaries and find matching IDs
            // Simplified: In a real app we'd store a list of product IDs in the Order entity.
            // For now, let's just pick some products if there are any orders.
            if (orders.isNotEmpty()) {
                // Mock logic: Get first 5 products from catalog as "previously bought"
                val productIds = listOf(1, 101, 105, 10) 
                viewModel.updateBuyItAgain(productIds)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }
}
