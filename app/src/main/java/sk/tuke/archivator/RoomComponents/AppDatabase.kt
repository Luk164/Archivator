package sk.tuke.archivator.RoomComponents

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.tuke.archivator.Entities.Item

@Database(entities = [Item::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}