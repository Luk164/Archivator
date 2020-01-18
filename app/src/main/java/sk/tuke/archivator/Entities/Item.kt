package sk.tuke.archivator.Entities

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.room.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "AcquisitionDate") val date: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "desc") var desc: String = "",
    @ColumnInfo(name = "images") val images: MutableList<Uri> = ArrayList(),
    @ColumnInfo(name = "files") val files: MutableList<Uri> = ArrayList()
) {
    /**
     * @param context Optional, if provided a toast will be triggered to inform user why function returned false
     */
    fun verify(context: Context? = null): Boolean { //fixme
        if (context != null) {
            if (name.isBlank()) {
                Toast.makeText(context, "ERROR: Name can not be blank!", Toast.LENGTH_SHORT).show()
            }

            if (desc.isBlank()) {
                Toast.makeText(context, "ERROR: Description can not be blank!", Toast.LENGTH_SHORT)
                    .show()
            }

            for (image in images) {
                try {
                    val inStream = context.contentResolver.openInputStream(image)
                    inStream?.close()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Failed to open file at " + image.path,
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }
//                var bytes: ByteArray
//                val buffer = ByteArray(8192)
//                var bytesRead: Int
//                val output = ByteArrayOutputStream()
//                try {
//                    while (inStream?.read(buffer).also { bytesRead = it!! } != -1)
//                    {
//                        output.write(buffer, 0, bytesRead)
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                bytes = output.toByteArray()
//                val encodedString= Base64.encodeToString(bytes, Base64.DEFAULT)
            }
        }

        if (name.isNotBlank() && desc.isNotBlank() && id == 0L) {
            return true
        }
        return false
    }
}