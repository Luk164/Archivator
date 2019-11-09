package sk.tuke.archivator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_new_entry.view.*
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.Context
import android.widget.DatePicker
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_new_entry.*
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


/**
 * A simple [Fragment] subclass.
 */
class NewEntry : Fragment() {

    private val entry : ItemKT = ItemKT()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_new_entry, container, false)

        view.button_add_entry.setOnClickListener {
//            val item : ItemKT = ItemKT(view.text_name.text.toString(), view.text_date.text)
        }
        return view
    }

    override fun onResume() {
        super.onResume()

        text_date.setOnClickListener {
            val dpd = DatePickerDialog(activity!!)
            dpd.setOnDateSetListener { view, year, month, dayOfMonth ->
                entry.Date.set(year, month, dayOfMonth)
                text_date.setText(entry.Date.time.toString())
            }

            dpd.show()
        }
    }




    //Trash

//    val entry: ItemKT = ItemKT()
//    var cal = Calendar.getInstance()
//    var textview_date: TextView? = null

//    override fun onResume() {
//        super.onResume()
//
//        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
//            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
//                                   dayOfMonth: Int) {
//                cal.set(Calendar.YEAR, year)
//                cal.set(Calendar.MONTH, monthOfYear)
//                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//                updateDateInView()
//            }
//        }
//
//        view!!.text_date.setOnClickListener {
//            DatePickerDialog(
//                activity as Context, dateSetListener,
//                // set DatePickerDialog to point to today's date when it loads up
//                cal.get(Calendar.YEAR),
//                cal.get(Calendar.MONTH),
//                cal.get(Calendar.DAY_OF_MONTH)).show()
//        }
//    }
//
//    private fun updateDateInView() {
//        val myFormat = "MM/dd/yyyy" // mention the format you need
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        textview_date!!.text = sdf.format(cal.getTime())
//    }
}
