package sk.tuke.archivator.RoomComponents.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import sk.tuke.archivator.Entities.Image
import java.lang.Exception

@Dao
abstract class ImageDao
{
    @Query("SELECT * FROM images")
    abstract fun getAllLive(): LiveData<List<Image>>

    @Query("SELECT * FROM images")
    abstract fun getAllSync():List<Image>

    @Query("SELECT * FROM images WHERE ID IN (:imageIds)")
    abstract fun getAllByIdsLive(imageIds: IntArray): LiveData<List<Image>>

    @Query("SELECT * FROM images WHERE ID IN (:imageIds)")
    abstract fun getAllByIds(imageIds: IntArray): List<Image>

    @Query("SELECT * FROM images WHERE ID == :imageId")
    abstract fun getOneByIdLive(imageId: Long): LiveData<Image>

    @Query("SELECT * FROM images WHERE ID == :imageId")
    abstract fun getOneById(imageId: Long): Image

    @Query("SELECT COUNT(id) FROM images")
    abstract fun getImageCountLive(): LiveData<Long>

    @Query("SELECT COUNT(id) FROM items WHERE id like :itemId")
    protected abstract fun _checkItemExists(itemId: Long): Int

    fun insertImagesForItem(itemId: Long, images: List<Image>)
    {
        if (_checkItemExists(itemId) != 1)
        {
            throw Exception("Parent item_content ID not found in database!")
        }

        for (image in images)
        {
            image.parentItemId = itemId
            _insert(image)
        }
    }

    fun insertImageForItem(itemId: Long, image: Image): Long
    {
        if (_checkItemExists(itemId) != 1)
        {
            throw Exception("Parent item_content ID not found in database!")
        }

        image.parentItemId = itemId
        return _insert(image)
    }

    @Insert
    protected abstract fun _insertAll(images: List<Image>): List<Long>

    @Insert
    protected abstract fun _insert(Image: Image): Long

    @Delete
    abstract fun delete(Image: Image)

    @Query("DELETE FROM images WHERE id = (:imageId)")
    abstract fun delete(imageId: Long)
}