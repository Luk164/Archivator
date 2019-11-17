package sk.tuke.archivator.Entities

import java.util.Calendar
import android.net.Uri
import androidx.room.*

//class Item(_name: String = "N/A", _date: Calendar = Calendar.getInstance(), _description: String = "N/A") {
//    public val Name : String = _name
//    public val Date : Calendar = _date
//    public val Description : String = _description
//}

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "AcquisitionDate") val Date: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "Description") val Description: String = "",
    @ColumnInfo(name = "Images") val Images: List<Uri> = ArrayList())
{
    fun checkValid(): Boolean
    {
        if (name.isNotBlank() && Description.isNotBlank() && id==0)
        {
            return true
        }
        return false
    }
}