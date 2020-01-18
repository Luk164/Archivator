package sk.tuke.archivator.RoomComponents

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import sk.tuke.archivator.Entities.Event
import java.lang.Exception

@Dao
abstract class EventDao {
    @Query("SELECT * FROM events")
    abstract fun getAll(): List<Event>

    @Query("SELECT * FROM events")
    abstract fun getAllLive(): LiveData<List<Event>>

    @Query("SELECT COUNT(id) FROM items WHERE id like :itemId")
    protected abstract fun _checkItemExists(itemId: Long): Int

    fun insertEventsForItem(itemId: Long, events: List<Event>)
    {
        if (_checkItemExists(itemId) != 1)
        {
            throw Exception("Parent item ID not found in database!")
        }

        for (event in events)
        {
            event.parentItemId = itemId
            _insert(event)
        }
    }

    @Insert
    abstract fun _insertAll(events: List<Event>): List<Long>

    @Insert
    abstract fun _insert(event: Event): Long

    @Delete
    abstract fun delete(event: Event)
}