package sk.tuke.archivator.RoomComponents

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sk.tuke.archivator.Entities.Event
import sk.tuke.archivator.Entities.FileEntity
import sk.tuke.archivator.Entities.Image
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.RoomComponents.Daos.EventDao
import sk.tuke.archivator.RoomComponents.Daos.FileDao
import sk.tuke.archivator.RoomComponents.Daos.ImageDao
import sk.tuke.archivator.RoomComponents.Daos.ItemDao

@Database(entities = [Item::class, Event::class, Image::class, FileEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun eventDao(): EventDao
    abstract fun imageDao(): ImageDao
    abstract fun fileDao(): FileDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //I don't get why do we pass context every damn time but whatever
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ArchivatorDB"
                ).apply {
                    this.fallbackToDestructiveMigration() //Fixme before release
                    //this.enableMultiInstanceInvalidation() //Synchronises multiple instances of room
                }.build()
                INSTANCE = instance
                return instance
            }
        }
    }
}