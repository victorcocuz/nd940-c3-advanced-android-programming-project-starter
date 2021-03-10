package com.udacity

object Constants {
    const val TITLE = "title"
    const val STATUS = "status"
}

enum class Link(val title: Int, val url: String) {
    GLIDE(R.string.radio_button_glide, "https://github.com/bumptech/glide"),
    LOAD_APP(R.string.radio_button_load_app, "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"),
    RETROFIT(R.string.radio_button_retrofit, "https://github.com/square/retrofit")
}