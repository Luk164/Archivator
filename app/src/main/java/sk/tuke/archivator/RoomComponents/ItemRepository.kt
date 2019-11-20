package sk.tuke.archivator.RoomComponents

import androidx.lifecycle.LiveData
import sk.tuke.archivator.Entities.Item

//I really dont get the point of this design pattern

//class ItemRepository(private val itemDao: ItemDao) {
//    val getAll: LiveData<List<Item>> = itemDao.getAll()
//
//    fun getAllById(itemsIds: IntArray): LiveData<List<Item>>
//    {
//        return itemDao.getAllByIds(itemsIds)
//    }
//
//    fun insert(item: Item) {
//        itemDao.insert(item)
//    }
//
//    fun delete(item: Item)
//    {
//        itemDao.delete(item)
//    }
//}