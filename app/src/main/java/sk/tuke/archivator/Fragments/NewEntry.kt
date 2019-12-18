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
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Global
import sk.tuke.archivator.MainActivity
import sk.tuke.archivator.R
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.RoomComponents.ImageListAdapter
import sk.tuke.archivator.ViewModels.ItemViewModel
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class NewEntry : Fragment() {

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_new_entry, container, false)

        itemViewModel = activity?.run {
            ViewModelProviders.of(this)[ItemViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        view.button_add_entry.setOnClickListener {
            if(itemViewModel.tmpItem.value!!.checkValid(activity!!))
            {
                val item = itemViewModel.tmpItem.value
                if (item != null) {
                    //itemViewModel.tmpItem.value = Item()
                    CoroutineScope(Dispatchers.Default).launch {
                        AppDatabase.getDatabase(activity!!).itemDao().insertAll(item)
                    }
                }
                else
                {
                    Toast.makeText(activity!!, "Item saving failed", Toast.LENGTH_SHORT).show()
                }

                view.findNavController().popBackStack() //end fragment after adding to database
            }
        }

        view.button_image.setOnClickListener {
            pickFromGallery()
        }

        val adapter = ImageListAdapter(activity!!)
        view.rv_images.adapter = adapter
        view.rv_images.layoutManager = LinearLayoutManager(activity!!)
        itemViewModel.tmpItem.observe(this, androidx.lifecycle.Observer {
            it?.let {
                adapter.setItem(it, itemViewModel)
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).title = getString(R.string.new_entry)

        if (itemViewModel.tmpItem.value!!.name.isNotEmpty()){
            text_name.setText(itemViewModel.tmpItem.value!!.name)
        }
        if (itemViewModel.tmpItem.value!!.desc.isNotEmpty()){
            text_desc.setText(itemViewModel.tmpItem.value!!.desc)
        }
        if (itemViewModel.tmpItem.value!!.date.isSet(Calendar.DATE))
        {
            text_date.setText(Global.dateFormatter.format(itemViewModel.tmpItem.value!!.date.time))
        }

//        for (imgUri in itemViewModel.tmpItem.images)
//        {
//            ImageView(activity).apply {
//                this.setImageURI(imgUri)
//                this.setOnClickListener {
//                    scrollView_layout.removeView(it)
//                }
//            }.let {
//                scrollView_layout.addView(it)
//            }
//        }

        text_name.doOnTextChanged { text, start, count, after ->
            itemViewModel.tmpItem.value!!.name = text.toString()
        }
        text_desc.doOnTextChanged { text, start, count, after ->
            itemViewModel.tmpItem.value!!.desc = text.toString()
        }

        text_date.setOnClickListener {
            val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                itemViewModel.tmpItem.value!!.date.set(year, month, dayOfMonth)
                text_date.setText(Global.dateFormatter.format(itemViewModel.tmpItem.value!!.date.time)) //weird way to do it but it works. There were problems with android.icu implementation
            }, 2019, 1, 1).run {this.show()}
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

        // When an Image is picked
        if (requestCode == Global.GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            when {
                //multiple images
                data.clipData != null -> {
                    val uriList: MutableList<Uri> = mutableListOf()
                    for (i in 0 until data.clipData!!.itemCount) //ugly as sin but cant be done better
                    {
                        uriList.add(data.clipData!!.getItemAt(i).uri)
//                        ImageView(activity).apply {
//                            this.setImageURI(data.clipData!!.getItemAt(i).uri.apply {
//                                uriList.add(this)
//                            })
//                            this.setOnClickListener {
//                                scrollView_layout.removeView(it)
//                            }
//                        }.let {
//                            scrollView_layout.addView(it)
//                        }
                    }
                    itemViewModel.tmpItem.value!!.images.addAll(uriList)
                    itemViewModel.tmpItem.postValue(itemViewModel.tmpItem.value)
                }
                //Single image
                data.data != null -> {
//                    ImageView(activity).apply {
//                        this.setImageURI(data.data!!)
//                        this.setOnClickListener {
////                            scrollView_layout.removeView(this)
//                            (this.parent as ViewGroup).removeView(it)
//                            itemViewModel.tmpItem.images.remove(data.data!!)
//                        }
//                    }.let {
//                        scrollView_layout.addView(it)
//                    }
                    itemViewModel.tmpItem.value!!.images.add(data.data!!)
                    itemViewModel.tmpItem.postValue(itemViewModel.tmpItem.value)
                }
                else ->
                {
                    Log.e("Image selection failed", "Did you not select any image?")
                    Toast.makeText(activity!!, getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}