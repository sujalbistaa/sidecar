package com.sidecar.app.ui

/**
 * Four tabs, no navigation library. A sealed route graph would be more than
 * this app needs and another dependency in the APK.
 */
enum class Tab(val label: String) {
    REVIEW("REVIEW"),
    LOG("LOG"),
    MODEL("MODEL"),
    SESSION("SESSION"),
}
