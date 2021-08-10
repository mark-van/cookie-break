package com.example.cookiebreak.model

import androidx.lifecycle.*
import com.example.cookiebreak.database.History
import com.example.cookiebreak.database.HistoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import java.text.SimpleDateFormat

//viewmodel for history activity

//for data persistance

class EatenCookiesModel(private val historyDao: HistoryDao) : ViewModel() {

    var randomInt: Int = 0

    var buttons: Int = 0

    // Cache all items form the database using LiveData.
    val allItems: LiveData<List<History>> = fullHistory().asLiveData()

    //can only me set by the activity instantiating EatenCookiesModel
    var select = MutableLiveData<Boolean>(false)

    var selectList = mutableListOf<posIdPair>()


    fun fullHistory(): Flow<List<History>> = historyDao.getAll()

    private fun insertHistory(history: History){
        viewModelScope.launch {
            historyDao.insert(history)
        }
    }
    fun deleteAll(){
        viewModelScope.launch {
            historyDao.deleteAll()
        }
    }
    fun deleteHistory(id: Int){
        viewModelScope.launch {
            historyDao.deleteHistory(id)
        }
    }

    fun addNewItem(cookiePortion: Int) {
        val newItem = getNewItemEntry(cookiePortion)
        insertHistory(newItem)
    }
//    fun isEntryValid(cookiePortion: Int, cookieTime: Int): Boolean {
//        if ((cookiePortion >= 0) && (cookiePortion <= 8)) {
//            return false
//        }
//        return true
//    }

    private fun getNewItemEntry(cookiePortion: Int): History {
        return History(
            cookiePortion = cookiePortion
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

class posIdPair(val pos:Int, val id:Int)
//
//class myList(override val size: Int) : MutableList<posIdPair>{
//    fun contains(pos: Int): Boolean {
//        return true
//    }

