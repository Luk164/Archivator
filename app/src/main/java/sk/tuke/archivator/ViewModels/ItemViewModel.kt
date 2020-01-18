package sk.tuke.archivator.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.RoomComponents.ItemDao

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    val itemDao: ItemDao = AppDatabase.getDatabase(application).itemDao()
}