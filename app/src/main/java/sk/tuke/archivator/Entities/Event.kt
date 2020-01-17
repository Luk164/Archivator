package sk.tuke.archivator.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "events")
data class Event(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                 @ColumnInfo(name = "name") var name: String = "",
                 @ColumnInfo(name = "AcquisitionDate") val date: Calendar = Calendar.getInstance())
{
    /**
     * Returns true if event is properly set
     */
    public fun validate(): Boolean {
        if (this.name.isNotEmpty())
        {
            return true
        }
        return false
    }
}