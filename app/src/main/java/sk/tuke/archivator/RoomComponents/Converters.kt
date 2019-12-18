package sk.tuke.archivator.RoomComponents

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*
import com.google.gson.reflect.TypeToken
import sk.tuke.archivator.Global


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
    fun StringToUriList(string: String): List<Uri>
    {
        val uriList = mutableListOf<Uri>()
        val stringList = Global.gson.fromJson<List<String>>(string, object: TypeToken<List<String>>() {}.type)

        for (str in stringList)
        {
            uriList.add(Uri.parse(str))
        }

        return uriList
}

    @TypeConverter
    fun UriListToString(uriList: List<Uri>): String
    {
        val stringList = mutableListOf<String>()
        for (uri in uriList)
        {
            stringList.add(uri.toString())
        }
        return Global.gson.toJson(stringList)
    }
}