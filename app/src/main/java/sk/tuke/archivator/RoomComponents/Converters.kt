package sk.tuke.archivator.RoomComponents

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*
import kotlin.collections.ArrayList
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
    fun stringToSomeObjectList(data: String?): List<Uri> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Uri>>() {}.type //WTF

        return Global.gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Uri>): String {
        return Global.gson.toJson(someObjects)
    }
}