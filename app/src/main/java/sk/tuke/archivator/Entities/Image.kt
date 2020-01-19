package sk.tuke.archivator.Entities

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.Exception

@Entity(tableName = "images")
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var uri: Uri,
    var description: String,
    var parentItemId: Long
){
    public fun verify(context: Context): Boolean
    {
        try {
            val inStream = context.contentResolver.openInputStream(uri)
            inStream?.close()
        } catch (e: Exception) {
            Toast.makeText(context,"Failed to open file at " + uri.path, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}