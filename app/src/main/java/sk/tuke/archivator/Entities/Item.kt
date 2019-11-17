package sk.tuke.archivator.Entities

import java.util.Calendar
import android.net.Uri
import androidx.room.*

//class Item(_name: String = "N/A", _date: Calendar = Calendar.getInstance(), _description: String = "N/A") {
//    public val Name : String = _name
//    public val date : Calendar = _date
//    public val desc : String = _description
//}

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "AcquisitionDate") val date: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "desc") var desc: String = "",
    @ColumnInfo(name = "images") val images: List<Uri> = ArrayList())
{
    fun checkValid(): Boolean
    {
        if (name.isNotBlank() && desc.isNotBlank() && id==0)
        {
            return true
        }
        return false
    }
}