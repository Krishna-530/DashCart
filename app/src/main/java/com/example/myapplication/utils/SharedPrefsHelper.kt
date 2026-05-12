package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsHelper {
    private const val PREFS_NAME    = "grocery_app_prefs"
    private const val KEY_LOGGED_IN = "is_logged_in"
    private const val KEY_ADDRESS   = "saved_address"
    private const val KEY_NAME      = "user_name"
    private const val KEY_EMAIL     = "user_email"
    private const val KEY_MOBILE    = "user_mobile"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_HOUSE_NO  = "address_house"
    private const val KEY_STREET    = "address_street"
    private const val KEY_CITY      = "address_city"
    private const val KEY_PINCODE   = "address_pincode"
    private const val KEY_WELCOME_OFFER = "welcome_offer_shown"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ── Login ────────────────────────────────────────────────────────────────
    fun setLoggedIn(context: Context, v: Boolean) = prefs(context).edit().putBoolean(KEY_LOGGED_IN, v).apply()
    fun isLoggedIn(context: Context)              = prefs(context).getBoolean(KEY_LOGGED_IN, false)

    // ── Basic profile ────────────────────────────────────────────────────────
    fun saveName(context: Context, v: String)   = prefs(context).edit().putString(KEY_NAME, v).apply()
    fun getName(context: Context)               = prefs(context).getString(KEY_NAME, "") ?: ""

    fun saveEmail(context: Context, v: String)  = prefs(context).edit().putString(KEY_EMAIL, v).apply()
    fun getEmail(context: Context)              = prefs(context).getString(KEY_EMAIL, "") ?: ""

    fun saveMobile(context: Context, v: String) = prefs(context).edit().putString(KEY_MOBILE, v).apply()
    fun getMobile(context: Context)             = prefs(context).getString(KEY_MOBILE, "") ?: ""

    // ── Dark mode ────────────────────────────────────────────────────────────
    fun setDarkMode(context: Context, v: Boolean) = prefs(context).edit().putBoolean(KEY_DARK_MODE, v).apply()
    fun isDarkMode(context: Context)              = prefs(context).getBoolean(KEY_DARK_MODE, false)

    // ── Full delivery address ────────────────────────────────────────────────
    fun saveAddress(context: Context, v: String) = prefs(context).edit().putString(KEY_ADDRESS, v).apply()
    fun getSavedAddress(context: Context)        = prefs(context).getString(KEY_ADDRESS, "") ?: ""

    fun saveHouseNo(context: Context, v: String) = prefs(context).edit().putString(KEY_HOUSE_NO, v).apply()
    fun getHouseNo(context: Context)             = prefs(context).getString(KEY_HOUSE_NO, "") ?: ""

    fun saveStreet(context: Context, v: String)  = prefs(context).edit().putString(KEY_STREET, v).apply()
    fun getStreet(context: Context)              = prefs(context).getString(KEY_STREET, "") ?: ""

    fun saveCity(context: Context, v: String)    = prefs(context).edit().putString(KEY_CITY, v).apply()
    fun getCity(context: Context)                = prefs(context).getString(KEY_CITY, "") ?: ""

    fun savePincode(context: Context, v: String) = prefs(context).edit().putString(KEY_PINCODE, v).apply()
    fun getPincode(context: Context)             = prefs(context).getString(KEY_PINCODE, "") ?: ""

    // ── Active Order Tracking (Feature 4) ────────────────────────────────────
    private const val KEY_ACTIVE_ORDER_ID    = "active_order_id"
    private const val KEY_ACTIVE_ORDER_TIME  = "active_order_time"

    fun saveActiveOrder(context: Context, orderId: String, startTime: Long) {
        prefs(context).edit()
            .putString(KEY_ACTIVE_ORDER_ID, orderId)
            .putLong(KEY_ACTIVE_ORDER_TIME, startTime)
            .apply()
    }

    fun clearActiveOrder(context: Context) {
        prefs(context).edit()
            .remove(KEY_ACTIVE_ORDER_ID)
            .remove(KEY_ACTIVE_ORDER_TIME)
            .apply()
    }

    fun getActiveOrderId(context: Context)   = prefs(context).getString(KEY_ACTIVE_ORDER_ID, null)
    fun getActiveOrderTime(context: Context) = prefs(context).getLong(KEY_ACTIVE_ORDER_TIME, 0L)

    // ── Offers ──
    fun hasSeenWelcomeOffer(context: Context) = prefs(context).getBoolean(KEY_WELCOME_OFFER, false)
    fun setHasSeenWelcomeOffer(context: Context, v: Boolean) = prefs(context).edit().putBoolean(KEY_WELCOME_OFFER, v).apply()
}
