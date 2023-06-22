package com.github.user.soilitouraplication.ui.faq.adapter

data class SectionItem(
    val title: String,
    val color: Int,
    val child: String,
    var expanded: Boolean = false
)