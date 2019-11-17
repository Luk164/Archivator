package sk.tuke.archivator.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import sk.tuke.archivator.Entities.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAll(): List<Item>

    @Query("SELECT * FROM items WHERE ID IN (:itemIds)")
    fun loadAllByIds(itemIds: IntArray): List<Item>

    @Query("SELECT * FROM items WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Item

    @Insert
    fun insertAll(vararg items: Item)

    @Delete
    fun delete(item: Item)
}