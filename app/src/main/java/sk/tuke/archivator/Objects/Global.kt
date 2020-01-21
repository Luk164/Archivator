package sk.tuke.archivator.Objects

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import sk.tuke.archivator.Utils.VolleyNetworkManager
import java.text.SimpleDateFormat

object Global {
    const val GALLERY_REQUEST_CODE = 1
    const val FILE_REQUEST_CODE = 2
    lateinit var dateFormatter : SimpleDateFormat
    val gson = Gson()
    lateinit var VNM: VolleyNetworkManager

    /**
     * Uses [uri] to get name of file and returns that name. Requires [Context].
     */
    fun getFileName(uri: Uri, context: Context): String
    {
        var result: String? = null
        if (uri.scheme.equals("content"))
        {
            val cursor: Cursor = context.contentResolver.query(uri, null, null, null, null)!!
            cursor.use { cursor ->
                if (cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
            if (!cursor.isClosed)
            {
                cursor.close()
            }
        }
        if (result == null) //weird
        {
            result = uri.getPath()
            val cut = result!!.lastIndexOf('/')
            if (cut != -1)
            {
                result = result!!.substring(cut + 1)
            }
        }
        return result.toString()
    }
}