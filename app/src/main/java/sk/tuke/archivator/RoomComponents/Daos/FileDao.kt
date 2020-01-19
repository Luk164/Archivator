package sk.tuke.archivator.RoomComponents.Daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import sk.tuke.archivator.Entities.FileEntity
import java.lang.Exception

@Dao
abstract class FileDao
{
    @Query("SELECT * FROM files")
    abstract fun getAllLive(): LiveData<List<FileEntity>>

    @Query("SELECT * FROM files")
    abstract fun getAllSync():List<FileEntity>

    @Query("SELECT * FROM files WHERE ID IN (:fileIds)")
    abstract fun getAllByIdsLive(fileIds: IntArray): LiveData<List<FileEntity>>

    @Query("SELECT * FROM files WHERE ID IN (:fileIds)")
    abstract fun getAllByIds(fileIds: IntArray): List<FileEntity>

    @Query("SELECT * FROM files WHERE ID == :fileId")
    abstract fun getOneByIdLive(fileId: Long): LiveData<FileEntity>

    @Query("SELECT * FROM files WHERE ID == :fileId")
    abstract fun getOneById(fileId: Long): FileEntity

    @Query("SELECT COUNT(id) FROM files")
    abstract fun getfileCountLive(): LiveData<Long>

    @Query("SELECT COUNT(id) FROM items WHERE id like :itemId")
    protected abstract fun _checkItemExists(itemId: Long): Int

    fun insertFilesForItem(itemId: Long, files: List<FileEntity>): List<Long>
    {
        if (_checkItemExists(itemId) != 1)
        {
            throw Exception("Parent file ID not found in database!")
        }

        val newFileIds = mutableListOf<Long>()
        for (FileEntity in files)
        {
            FileEntity.parentItemId = itemId
            newFileIds.add(_insert(FileEntity))
        }
        return newFileIds
    }

    fun insertFileForItem(itemId: Long, FileEntity: FileEntity): Long
    {
        if (_checkItemExists(itemId) != 1)
        {
            throw Exception("Parent file ID not found in database!")
        }

        FileEntity.parentItemId = itemId
        return _insert(FileEntity)
    }

    @Insert
    protected abstract fun _insertAll(files: List<FileEntity>): List<Long>

    @Insert
    protected abstract fun _insert(FileEntity: FileEntity): Long

    @Delete
    abstract fun delete(FileEntity: FileEntity)

    @Query("DELETE FROM files WHERE id = (:fileId)")
    abstract fun delete(fileId: Long)
}