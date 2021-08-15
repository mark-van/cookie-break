package com.markvangenderen.cookiebreak.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//schema for the databases sole relation(History)
@Entity
data class History(

    //treat this property as the primary key when new rows are inserted
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //@ColumnInfo annotation to specify a name for the column
    //@NonNull annotation since we don't want the column to be null
    @NonNull
    @ColumnInfo(name = "cookie_portion") val cookiePortion: Int,
    @NonNull @ColumnInfo(name = "cookie_time") val cookieTime: Long = System.currentTimeMillis()
)
