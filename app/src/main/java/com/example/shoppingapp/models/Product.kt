package com.example.shoppingapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    var user_id : String = "",
    var user_name : String = "",
    var title : String = "",
    var price : String = "",
    var description : String = "",
    var stock_quantity : String = "",
    var image : String = "",
    var product_id : String = ""
) : Parcelable
