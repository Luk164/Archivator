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
    fun getAllLive(): LiveData<List<Item>>

    @Query("SELECT * FROM items")
    fun getAllSync():List<Item>

    @Query("SELECT * FROM items WHERE ID IN (:itemIds)")
    fun getAllByIdsLive(itemIds: IntArray): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE ID IN (:itemIds)")
    fun getAllByIds(itemIds: IntArray): List<Item>

    @Query("SELECT * FROM items WHERE ID == :itemId")
    fun getOneByIdLive(itemId: Int): LiveData<Item>

    @Query("SELECT * FROM items WHERE ID == :itemId")
    fun getOneById(itemId: Int): Item

    @Query("SELECT * FROM items WHERE name LIKE :name LIMIT 1")
    fun findByNameLive(name: String): LiveData<Item>

    @Query("SELECT COUNT(id) FROM items")
    fun getItemCountLive(): LiveData<Int>

    @Insert
    fun insertAll(vararg items: Item)

    @Insert
    fun insert(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("DELETE FROM items WHERE id = (:itemId)")
    fun delete(itemId: Int)
}