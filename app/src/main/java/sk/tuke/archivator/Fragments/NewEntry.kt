package sk.tuke.archivator.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_new_entry.view.*
import android.app.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_new_entry.*
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Global
import sk.tuke.archivator.R
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.ViewModels.ItemViewModel


/**
 * A simple [Fragment] subclass.
 */
class NewEntry : Fragment() {

    private val entry: Item = Item()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_new_entry, container, false)

        view.button_add_entry.setOnClickListener {
            entry.name = text_name.text.toString()
            entry.desc = text_desc.text.toString()

            if(entry.checkValid(activity!!))
            {
                //fixme
                AsyncTask.execute {
                    AppDatabase.getDatabase(activity!!).itemDao().insertAll(entry)
                }

//                val model = activity?.run {
//                    ViewModelProviders.of(this)[ItemViewModel::class.java]
//                } ?: throw Exception("Invalid Activity")

                view.findNavController().popBackStack() //end fragment after adding to database
            }
        }

        view.button_image.setOnClickListener {
            pickFromGallery()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        text_date.setOnClickListener {
            val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                entry.date.set(year, month, dayOfMonth)
                text_date.setText(Global.dateFormatter.format(entry.date.time)) //weird way to do it but it works. There were problems with android.icu implementation
            }, 2019, 1, 1)

            dpd.show()
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, Global.GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        val imgSwitcher = ImageView(activity)
        // When an Image is picked
        if (requestCode == Global.GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            when {
                //multiple images
                data.clipData != null -> for (i in 0 until data.clipData!!.itemCount) //ugly as sin but cant be done better
                {
                    imgSwitcher.setImageURI(data.clipData!!.getItemAt(i).uri)
                    imgSwitcher.setOnClickListener {
                        scrollView_layout.removeView(imgSwitcher)
                    }
                    scrollView_layout.addView(imgSwitcher)
                }
                //Single image
                data.data != null -> {
                    imgSwitcher.setImageURI(data.data!!)
                    imgSwitcher.setOnClickListener {
                        scrollView_layout.removeView(imgSwitcher)
                    }
                    scrollView_layout.addView(imgSwitcher)
                }
                else ->
                {
                    Log.e("Image selection failed", "Did you not select any image?")
                    Toast.makeText(activity!!, "No image selected!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}