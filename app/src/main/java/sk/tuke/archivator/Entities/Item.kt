package sk.tuke.archivator.Entities

import android.content.Context
import java.util.Calendar
import android.net.Uri
import android.widget.Toast
import androidx.room.*

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "AcquisitionDate") val date: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "desc") var desc: String = "",
    @ColumnInfo(name = "images") val images: MutableList<Uri> = ArrayList())
{
    /**
     * @param context Optional, if provided a toast will be triggered to inform user why function returned false
     */
    fun checkValid(context: Context? = null): Boolean
    {
        if (context != null)
        {
            if (name.isBlank())
            {
                Toast.makeText(context, "ERROR: Name can not be blank!", Toast.LENGTH_SHORT).show()
            }

            if (desc.isBlank())
            {
                Toast.makeText(context, "ERROR: Description can not be blank!", Toast.LENGTH_SHORT).show()
            }
        }

        if (name.isNotBlank() && desc.isNotBlank() && id==0)
        {
            return true
        }
        return false
    }
}