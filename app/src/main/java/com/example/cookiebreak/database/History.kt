package com.example.cookiebreak.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(

    //treat this property as the primary key when new rows are inserted
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //@ColumnInfo annotation to specify a name for the column
    //@NonNull annotation since we don't want the column to be null
    @NonNull
    @ColumnInfo(name = "cookie_portion") val cookiePortion: Int,
    @NonNull @ColumnInfo(name = "cookie_time") val cookieTime: Int
)
// but for date
//fun Item.getFormattedPrice(): String =
//    NumberFormat.getCurrencyInstance().format(itemPrice)