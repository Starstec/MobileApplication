package com.example.starstec.data.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class Category(
    val name: String
): Parcelable

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class Question(
    val id: Int,
    val question: String,
    val category: Category? = null,
    val options: List<Option>,
    var number: String = "0",
    var answer: String? = null,
    val type: Type,
    val hint: String? = null
): Parcelable {

    @Parcelize
    data class Option(
        val answer: String,
        val point: Int,
        var checked: Boolean = false
    ): Parcelable

    @Parcelize
    enum class Type(val value: String) : Parcelable {
        OPTIONAL("optional"),
        ESSAY("essay"),
        NUMBER("number"),
        TIME("time");

        companion object {
            fun fromString(value: String): Type? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

}
