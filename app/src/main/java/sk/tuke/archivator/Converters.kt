package sk.tuke.archivator

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList



class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value?.let {
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = it
        }
    }

    @TypeConverter
    fun dateToTimestamp(calendar: Calendar?): Long? {
        return calendar?.timeInMillis
    }

    @TypeConverter
    fun uriToString(uri: Uri?): String
    {
        return uri.toString()
    }

    @TypeConverter
    fun stringToUri(string: String): Uri
    {
        return Uri.parse(string)
    }

    @TypeConverter
    fun ListToString(list: ArrayList<Uri>): String
    {
        return ""
    }

    @TypeConverter
    fun StringToList(string: String): ArrayList<Uri>
    {
        return ArrayList()
    }

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Uri> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Uri>>() {

        }.type

        return Global.gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Uri>): String {
        return Global.gson.toJson(someObjects)
    }
}