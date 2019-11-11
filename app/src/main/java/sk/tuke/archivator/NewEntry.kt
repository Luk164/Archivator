package sk.tuke.archivator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_new_entry.view.*
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import kotlinx.android.synthetic.main.fragment_new_entry.*
import android.content.Intent
import android.app.Activity


/**
 * A simple [Fragment] subclass.
 */
class NewEntry : Fragment() {

    private val entry: ItemKT = ItemKT()
    val GALLERY_REQUEST_CODE = 1
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd G")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_new_entry, container, false)

        view.button_add_entry.setOnClickListener {
            //            val item : ItemKT = ItemKT(view.text_name.text.toString(), view.text_date.text)
        }

        view.button_image.setOnClickListener {
            pickFromGallery()
        }

        view.button_addImage2.setOnClickListener {
            pickFromGallery2()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        text_date.setOnClickListener {
            val dpd = DatePickerDialog(activity!!)
            dpd.setOnDateSetListener { view, year, month, dayOfMonth ->
                entry.Date.set(year, month, dayOfMonth)
                text_date.setText(dateFormatter.format(entry.Date))
            }

            dpd.show()
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun pickFromGallery2() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    //data.getData returns the content URI for the selected Image
                    val selectedImage = data!!.data
                    imageView_entry.setImageURI(selectedImage)
                }
            }
    }
}

//DIFFERENT APPROACHES TO GETTING PICTURES

//if (Build.VERSION.SDK_INT < 19) {
//    var intent = Intent()
//    intent.type = "image/*"
//    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//    intent.action = Intent.ACTION_GET_CONTENT
//    startActivityForResult(
//        Intent.createChooser(intent, "Select Picture")
//        , PICK_IMAGE_MULTIPLE
//    )
//} else {
//    var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//    intent.addCategory(Intent.CATEGORY_OPENABLE)
//    intent.type = "image/*"
//    startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
//}

//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//    super.onActivityResult(requestCode, resultCode, data)
//    // When an Image is picked
//    if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
//        && null != data
//    ) {
//        if (data.getClipData() != null) {
//            var count = data.clipData.itemCount
//            for (i in 0..count - 1) {
//                var imageUri: Uri = data.clipData.getItemAt(i).uri
//                getPathFromURI(imageUri)
//            }
//        } else if (data.getData() != null) {
//            var imagePath: String = data.data.path
//            Log.e("imagePath", imagePath);
//        }
//
//        displayImageData()
//    }
//}
//
//private fun getPathFromURI(uri: Uri) {
//    var path: String = uri.path // uri = any content Uri
//
//    val databaseUri: Uri
//    val selection: String?
//    val selectionArgs: Array<String>?
//    if (path.contains("/document/image:")) { // files selected from "Documents"
//        databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        selection = "_id=?"
//        selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
//    } else { // files selected from all other sources, especially on Samsung devices
//        databaseUri = uri
//        selection = null
//        selectionArgs = null
//    }
//    try {
//        val projection = arrayOf(
//            MediaStore.Images.Media.DATA,
//            MediaStore.Images.Media._ID,
//            MediaStore.Images.Media.ORIENTATION,
//            MediaStore.Images.Media.DATE_TAKEN
//        ) // some example data you can query
//        val cursor = contentResolver.query(
//            databaseUri,
//            projection, selection, selectionArgs, null
//        )
//        if (cursor.moveToFirst()) {
//            val columnIndex = cursor.getColumnIndex(projection[0])
//            imagePath = cursor.getString(columnIndex)
//            // Log.e("path", imagePath);
//            imagesPathList.add(imagePath)
//        }
//        cursor.close()
//    } catch (e: Exception) {
//        Log.e(TAG, e.message, e)
//    }
//}