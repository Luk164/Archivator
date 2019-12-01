package sk.tuke.archivator.RoomComponents

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import sk.tuke.archivator.Entities.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAll(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE ID IN (:itemIds)")
    fun getAllByIds(itemIds: IntArray): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE ID == :itemId")
    fun getOneById(itemId: Int): LiveData<Item>

    @Query("SELECT * FROM items WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): LiveData<Item>

    @Insert
    fun insertAll(vararg items: Item)

    @Insert
    fun insert(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("DELETE FROM items WHERE id = (:itemId)")
    fun delete(itemId: Int)
}