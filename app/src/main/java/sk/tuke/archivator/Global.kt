package sk.tuke.archivator

import android.icu.text.SimpleDateFormat

class Global {
    companion object {
        fun GALLERY_REQUEST_CODE() : Int = 1
        fun dateFormatter() : SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd G")
    }
}