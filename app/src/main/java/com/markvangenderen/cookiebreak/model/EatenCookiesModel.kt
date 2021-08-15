package com.markvangenderen.cookiebreak.model

import androidx.lifecycle.*
import com.markvangenderen.cookiebreak.database.History
import com.markvangenderen.cookiebreak.database.HistoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

//ViewModel model for MainActivity fragments
//implemented a shared ViewModel to simplify database management
class EatenCookiesModel(private val historyDao: HistoryDao) : ViewModel() {

    var randomInt: Int = 0

    var buttons: Int = 0

    // Cache all items form the database using LiveData.
    val allItems: LiveData<List<History>> = fullHistory().asLiveData()

    //can only me set by the activity instantiating EatenCookiesModel
    var select = MutableLiveData(false)

    var selectList = mutableListOf<PosIdPair>()


    private fun fullHistory(): Flow<List<History>> = historyDao.getAll()

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


    private fun getNewItemEntry(cookiePortion: Int): History {
        return History(
            cookiePortion = cookiePortion
        )
    }

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

class PosIdPair(val pos:Int, val id:Int)


