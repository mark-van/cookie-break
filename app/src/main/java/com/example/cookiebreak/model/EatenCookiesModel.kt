package com.example.cookiebreak.model

import androidx.lifecycle.*
import com.example.cookiebreak.database.History
import com.example.cookiebreak.database.HistoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//viewmodel for history activity

private var _selected = MutableLiveData<Boolean>(false)
//for data persistance

private val _selectList = MutableLiveData<MutableList<Int>>(mutableListOf<Int>())
//only make the het public to all
val selected: LiveData<Boolean> = _selected

val selectList: LiveData<MutableList<Int>> = _selectList

class EatenCookiesModel(private val historyDao: HistoryDao) : ViewModel() {

    //can only me set by the activity instantiating EatenCookiesModel
    var selecting: Boolean = false
        set(bool){
            _selected.value=bool
            field = bool
        }
    var selectingList: MutableList<Int> = mutableListOf<Int>()
        init{
            _selectList.value=selectingList
        }

    fun fullHistory(): Flow<List<History>> = historyDao.getAll()

    private fun insertHistory(history: History){
        viewModelScope.launch {
            historyDao.insert(history)
        }
    }

    fun addNewItem(cookiePortion: Int, cookieTime: Int) {
        val newItem = getNewItemEntry(cookiePortion, cookieTime)
        insertHistory(newItem)
    }
//    fun isEntryValid(cookiePortion: Int, cookieTime: Int): Boolean {
//        if ((cookiePortion >= 0) && (cookiePortion <= 8)) {
//            return false
//        }
//        return true
//    }

    private fun getNewItemEntry(cookiePortion: Int, cookieTime: Int): History {
        return History(
            cookiePortion = cookiePortion,
            cookieTime = cookieTime
        )
    }

//    fun insert(h: History){
//        historyDao.insert(h)
//    }

//    var selectButton: Boolean = true
//    var deleteButton: Boolean = false
//    var deleteAllButton: Boolean = true

}

class EatenCookiesModelFactory(private val historyDao: HistoryDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EatenCookiesModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EatenCookiesModel(historyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}