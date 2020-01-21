package sk.tuke.archivator.RoomComponents.Daos

import androidx.lifecycle.LiveData
import androidx.room.*
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Entities.ItemWithStuff

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
    fun getOneByIdLive(itemId: Long): LiveData<Item>

    @Query("SELECT * FROM items WHERE ID == :itemId")
    fun getOneById(itemId: Long): Item

    @Query("SELECT * FROM items WHERE name LIKE :name LIMIT 1")
    fun findByNameLive(name: String): LiveData<Item>

    @Query("SELECT COUNT(id) FROM items")
    fun getItemCountLive(): LiveData<Long>

    @Insert
    fun insertAll(items: List<Item>): List<Long>

    @Insert
    fun insert(item: Item): Long

    @Delete
    fun delete(item: Item)

    @Query("DELETE FROM items WHERE id = (:itemId)")
    fun delete(itemId: Long)

    //Item with stuff
    @Transaction
    @Query("SELECT * FROM items")
    fun getItemsWithStuff(): List<ItemWithStuff>

    //Item with stuff
    @Transaction
    @Query("SELECT * FROM items")
    fun getItemsWithStuffLive(): LiveData<List<ItemWithStuff>>

    @Transaction
    @Query("SELECT * FROM items WHERE id = (:itemId)")
    fun getItemWithStuff(itemId: Long): ItemWithStuff

    @Transaction
    @Query("SELECT * FROM items WHERE id = (:itemId)")
    fun getItemWithStuffLive(itemId: Long): LiveData<ItemWithStuff>
}