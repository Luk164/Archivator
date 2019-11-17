package sk.tuke.archivator

import com.google.gson.Gson
import java.text.SimpleDateFormat

object Global {
    val GALLERY_REQUEST_CODE = 1
    lateinit var dateFormatter : SimpleDateFormat
    lateinit var db : AppDatabase
    val gson = Gson()
}