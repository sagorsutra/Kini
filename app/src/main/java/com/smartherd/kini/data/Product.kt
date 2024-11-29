package com.smartherd.kini.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val price: String = "",
    val category: String = "",
    val offerCategory: String = "",
    val imageUrl: String = ""
) :  Parcelable

