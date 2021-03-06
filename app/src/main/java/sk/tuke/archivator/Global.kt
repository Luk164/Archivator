package sk.tuke.archivator

import com.google.gson.Gson
import sk.tuke.archivator.Utils.VolleyNetworkManager
import java.text.SimpleDateFormat

object Global {
    const val GALLERY_REQUEST_CODE = 1
    lateinit var dateFormatter : SimpleDateFormat
    val gson = Gson()
    lateinit var VNM: VolleyNetworkManager
}