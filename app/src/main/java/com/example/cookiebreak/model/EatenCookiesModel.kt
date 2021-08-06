package com.example.cookiebreak.model

import androidx.lifecycle.ViewModel

//viewmodel for history activity

private var _selected: Boolean = false
//for data persistance

private var _selectList: MutableList<Int> = mutableListOf<Int>()
//only make the het public to all
val selected: Boolean
    get() = _selected

val selectList: MutableList<Int>
    get() = _selectList

class EatenCookiesModel : ViewModel() {

    //can only me set by the activity instantiating EatenCookiesModel
    var selecting: Boolean = false
        set(bool){
            _selected=bool
            field = bool
        }
    var selectingList: MutableList<Int> = mutableListOf<Int>()
        init{
            _selectList=selectingList
        }

//    var selectButton: Boolean = true
//    var deleteButton: Boolean = false
//    var deleteAllButton: Boolean = true

}