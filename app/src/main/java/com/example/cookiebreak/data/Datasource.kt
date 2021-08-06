package com.example.cookiebreak.data

import com.example.cookiebreak.R
import com.example.cookiebreak.model.EatenCookies

class Datasource {


    fun loadEatenCookies():MutableList<EatenCookies>{
        return mutableListOf<EatenCookies>(
            EatenCookies(R.string.eanten_cookie1, R.string.eanten_cookie1_date),
            EatenCookies(R.string.eanten_cookie2, R.string.eanten_cookie2_date),
            EatenCookies(R.string.eanten_cookie3, R.string.eanten_cookie3_date),
            EatenCookies(R.string.eanten_cookie4, R.string.eanten_cookie4_date),
            EatenCookies(R.string.eanten_cookie5, R.string.eanten_cookie5_date),
            EatenCookies(R.string.eanten_cookie6, R.string.eanten_cookie6_date)
        )
    }
}