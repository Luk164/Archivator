package sk.tuke.archivator

import java.util.Calendar
import android.net.Uri


//class Item(_name: String = "N/A", _date: Calendar = Calendar.getInstance(), _description: String = "N/A") {
//    public val Name : String = _name
//    public val Date : Calendar = _date
//    public val Description : String = _description
//}

data class Item(val id: Int = 0, val name: String = "", val Date: Calendar = Calendar.getInstance(), val Description: String = "", val Images: List<Uri> = ArrayList())