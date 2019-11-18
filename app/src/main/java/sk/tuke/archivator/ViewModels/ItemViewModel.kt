package sk.tuke.archivator.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.RoomComponents.ItemRepository

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository
    val allItemsLive: LiveData<List<Item>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val itemDao = AppDatabase.getDatabase(application, viewModelScope).itemDao()
        repository = ItemRepository(itemDao)
        allItemsLive = repository.getAll
    }

    fun insert(item: Item) = viewModelScope.launch {
        repository.insert(item)
    }

    fun delete(item: Item) = viewModelScope.launch {
        repository.delete(item)
    }

    //explanation needed
    fun getAllByIds(itemIds: IntArray): LiveData<List<Item>>
    {
        return repository.getAllById(itemIds)
    }
}