package sk.tuke.archivator

import android.icu.util.Calendar


class ItemKT(_name: String = "N/A", _date: Calendar = Calendar.getInstance(), _description: String = "N/A") {
    public val Name : String = _name
    public val Date : Calendar = _date
    public val Description : String = _description
}