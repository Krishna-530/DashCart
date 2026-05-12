package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Product
import com.example.myapplication.utils.DummyData

/**
 * HomeViewModel holds the state for the Home screen.
 *
 * Responsibilities:
 * 1. Load the full product list from DummyData
 * 2. Filter products by search query
 * 3. Filter products by selected category
 *
 * The Activity observes LiveData and simply renders whatever it receives.
 */
class HomeViewModel : ViewModel() {

    // All products (never changes after init)
    private val allProducts: List<Product> = DummyData.getProducts()

    // Products currently shown in the RecyclerView (filtered list)
    private val _filteredProducts = MutableLiveData<List<Product>>(allProducts)
    val filteredProducts: LiveData<List<Product>> = _filteredProducts

    val mostShoppedProducts: LiveData<List<Product>> = MutableLiveData(allProducts.filter { it.isFrequentlyPurchased })
    val newlyAddedProducts: LiveData<List<Product>> = MutableLiveData(allProducts.filter { it.isNewlyAdded })
    val highestDiscountProducts: LiveData<List<Product>> = MutableLiveData(allProducts.filter { it.isHighestDiscount })

    private val _buyItAgainProducts = MutableLiveData<List<Product>>(emptyList())
    val buyItAgainProducts: LiveData<List<Product>> = _buyItAgainProducts

    // Currently selected category chip ("All" means no filter)
    private var selectedCategory = "All"
    
    // Feature 7: Dietary filter
    private var selectedDietaryTag = "All"

    // Current search query
    private var searchQuery = ""

    /** Called when the user types in the search bar. */
    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        applyFilters()
    }

    /** Called when the user taps a category chip. */
    fun onCategorySelected(category: String) {
        selectedCategory = category
        applyFilters()
    }

    /** Feature 7: Filter by dietary tag */
    fun onDietaryTagSelected(tag: String) {
        selectedDietaryTag = tag
        applyFilters()
    }

    /** Feature 5: Set Buy It Again products based on order history */
    fun updateBuyItAgain(productIds: List<Int>) {
        val uniqueIds = productIds.distinct()
        val products = allProducts.filter { it.id in uniqueIds }
        _buyItAgainProducts.value = products
    }

    /**
     * Applies both the category filter and the search query filter together.
     * Steps:
     *   1. Start with all products
     *   2. If category != "All", keep only matching items
     *   3. If search query is non-empty, keep only name-matching items
     */
    private fun applyFilters() {
        var result = allProducts

        if (selectedCategory != "All") {
            result = result.filter { it.category == selectedCategory }
        }

        if (searchQuery.isNotEmpty()) {
            result = result.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }

        if (selectedDietaryTag != "All") {
            result = result.filter { it.tags.contains(selectedDietaryTag) }
        }

        _filteredProducts.value = result
    }
}
