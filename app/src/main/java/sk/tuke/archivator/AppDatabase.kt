package sk.tuke.archivator

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Interfaces.ItemDao

@Database(entities = arrayOf(Item::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}