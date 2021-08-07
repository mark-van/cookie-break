package com.example.cookiebreak.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookiebreak.database.History
import kotlinx.coroutines.launch

class CookieBreakModel : ViewModel() {
    //The convention is to prefix the private property with an underscore
    var randomInt: Int = 0
//    val randomInt: Int
//        get() = _randomInt
    var buttons: Int = 0
//    val buttons: Int
//        get() = _buttons

}