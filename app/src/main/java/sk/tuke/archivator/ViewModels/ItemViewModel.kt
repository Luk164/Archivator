package sk.tuke.archivator.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.RoomComponents.Daos.ItemDao

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    val itemDao: ItemDao = AppDatabase.getDatabase(application).itemDao()
}