package sk.tuke.archivator.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "events")
data class Event(@PrimaryKey(autoGenerate = true) val id: Long = 0L,
                 @ColumnInfo(name = "name") var name: String = "",
                 @ColumnInfo(name = "AcquisitionDate") val date: Calendar = Calendar.getInstance(),
                 var parentItemId: Long = 0)
{
    /**
     * Returns true if event is properly set, does not need to be true before inserting to database
     */
    public fun verify(): Boolean {
        if (this.name.isNotEmpty() && this.parentItemId != 0L)
        {
            return true
        }
        return false
    }
}