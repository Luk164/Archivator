package sk.tuke.archivator

import com.google.gson.Gson
import sk.tuke.archivator.RoomComponents.AppDatabase
import java.text.SimpleDateFormat

object Global {
    const val GALLERY_REQUEST_CODE = 1
    lateinit var dateFormatter : SimpleDateFormat
    lateinit var db : AppDatabase //deleteme to be replaced by getDatabase
    val gson = Gson()
}