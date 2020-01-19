package sk.tuke.archivator.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_new_entry.view.*
import android.app.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_new_entry.*
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.tuke.archivator.Objects.NewItem
import sk.tuke.archivator.Objects.Global
import sk.tuke.archivator.MainActivity
import sk.tuke.archivator.R
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.RoomComponents.PictureListAdapter
import sk.tuke.archivator.ViewModels.ItemViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class NewEntry : Fragment()
{

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_new_entry, container, false)
        setHasOptionsMenu(true)

        itemViewModel = activity?.run {
            ViewModelProviders.of(this)[ItemViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        view.button_image.setOnClickListener {
            pickFromGallery()
        }

        view.button_file.setOnClickListener {
            pickFile()
        }

        val adapter = PictureListAdapter(activity!!)
        view.rv_images.adapter = adapter
        view.rv_images.layoutManager = LinearLayoutManager(activity!!)
        NewItem.tmpItem.observe(this, androidx.lifecycle.Observer {
            it?.let {
                adapter.setItem(it, itemViewModel)
            }
        })

        return view
    }

    override fun onResume()
    {
        super.onResume()
        (requireActivity() as MainActivity).title = getString(R.string.new_entry)

        if (NewItem.tmpItem.value!!.name.isNotEmpty())
        {
            text_name.setText(NewItem.tmpItem.value!!.name)
        }
        if (NewItem.tmpItem.value!!.desc.isNotEmpty())
        {
            text_desc.setText(NewItem.tmpItem.value!!.desc)
        }
        if (NewItem.tmpItem.value!!.date.isSet(Calendar.DATE))
        {
            text_date.setText(Global.dateFormatter.format(NewItem.tmpItem.value!!.date.time))
        }

        text_name.doOnTextChanged { text, _, _, _ ->
            NewItem.tmpItem.value!!.name = text.toString()
        }
        text_desc.doOnTextChanged { text, _, _, _ ->
            NewItem.tmpItem.value!!.desc = text.toString()
        }

        text_date.setOnClickListener {
            DatePickerDialog(
                activity!!,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    NewItem.tmpItem.value!!.date.set(year, month, dayOfMonth)
                    text_date.setText(Global.dateFormatter.format(NewItem.tmpItem.value!!.date.time)) //weird way to do it but it works. There were problems with android.icu implementation
                },
                2019,
                1,
                1
            ).run { this.show() }
        }
    }

    private fun pickFromGallery()
    {
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            this.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            this.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            this.addCategory(Intent.CATEGORY_OPENABLE)
            this.type = "image/*"
            startActivityForResult(this, Global.GALLERY_REQUEST_CODE)
        }
    }

    private fun pickFile()
    {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            this.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            this.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            this.addCategory(Intent.CATEGORY_OPENABLE)
            this.type = "*/*"
            startActivityForResult(this, Global.FILE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        // When an Image is picked
        if (requestCode == Global.GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            when
            {
                //multiple images
                data.clipData != null ->
                {
                    val uriList: MutableList<Uri> = mutableListOf()
                    for (i in 0 until data.clipData!!.itemCount)
                    {
                        uriList.add(data.clipData!!.getItemAt(i).uri)
                    }
                    NewItem.tmpItem.value!!.images.addAll(uriList)
                    NewItem.tmpItem.postValue(
                        NewItem.tmpItem.value)
                }
                //Single image
                data.data != null ->
                {
                    NewItem.tmpItem.value!!.images.add(data.data!!)
                    NewItem.tmpItem.postValue(
                        NewItem.tmpItem.value)
                }
                else ->
                {
                    Log.e("Image selection failed", "Did you not select any image?")
                    Toast.makeText(
                        activity!!,
                        getString(R.string.no_image_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else if (requestCode == Global.FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) //when a file is picked
        {
            when
            {
                //multiple files
                data.clipData != null ->
                {
                    val uriList: MutableList<Uri> = mutableListOf()
                    for (i in 0 until data.clipData!!.itemCount)
                    {
                        uriList.add(data.clipData!!.getItemAt(i).uri)
                    }
                    NewItem.tmpItem.value!!.files.addAll(uriList)
                    NewItem.tmpItem.postValue(
                        NewItem.tmpItem.value)
                }
                //Single file
                data.data != null ->
                {
                    NewItem.tmpItem.value!!.files.add(data.data!!)
                    NewItem.tmpItem.postValue(
                        NewItem.tmpItem.value)
                }
                else ->
                {
                    Log.e("File selection failed", "Did you not select any files?")
                    Toast.makeText(
                        activity!!,
                        getString(R.string.no_image_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.button_save)
        {
            if (NewItem.tmpItem.value!!.verify(activity!!)) //ready to save
            {
                CoroutineScope(Dispatchers.Default).launch {
                    val newItemId = AppDatabase.getDatabase(activity!!).itemDao()
                        .insert(NewItem.tmpItem.value!!)

                    AppDatabase.getDatabase(activity!!).eventDao()
                        .insertEventsForItem(newItemId, NewItem.tmpEvents.value!!)

                    AppDatabase.getDatabase(activity!!).imageDao()
                        .insertImagesForItem(newItemId, NewItem.tmpImages.value!!)

                    AppDatabase.getDatabase(activity!!).fileDao()
                        .insertFilesForItem(newItemId, NewItem.tmpFiles.value!!)
                }.invokeOnCompletion {
                    CoroutineScope(Dispatchers.Main).launch {
                        NewItem.clear()
                    }
                }
            } else
            {
                Toast.makeText(activity!!, "Item saving failed", Toast.LENGTH_SHORT).show()
            }
            view?.findNavController()?.popBackStack() //end fragment after adding to database
        }
        return super.onOptionsItemSelected(item)
    }
}