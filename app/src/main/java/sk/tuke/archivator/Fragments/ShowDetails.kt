package sk.tuke.archivator.Fragments


import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_show_details.*
import sk.tuke.archivator.MainActivity
import sk.tuke.archivator.Objects.NewItem
import sk.tuke.archivator.R
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.RoomComponents.EventListAdapter
import sk.tuke.archivator.RoomComponents.FileListAdapter
import sk.tuke.archivator.RoomComponents.PictureListAdapter
import sk.tuke.archivator.ViewModels.ItemViewModel


/**
 * A simple [Fragment] subclass.
 */
class ShowDetails : Fragment() {

    private val args: ShowDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_show_details, container, false)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        val passedID = args.ID
        tv_id.text = "${getString(R.string.item_id_is)}$passedID"

        val itemViewModel = activity?.run {
            ViewModelProvider(this).get(ItemViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val itemToShow = itemViewModel.itemDao.getItemWithStuffLive(passedID)

        itemToShow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tv_name.text = it.item.name
            tv_desc.text = it.item.desc
        })

        val imageAdapter = PictureListAdapter(activity!!)
        rv_images.adapter = imageAdapter
        rv_images.layoutManager = LinearLayoutManager(activity!!)
        itemToShow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                imageAdapter.setItem(it.images)
            }
        })

        val fileAdapter = FileListAdapter(activity!!)
        rv_files.adapter = fileAdapter
        rv_files.layoutManager = LinearLayoutManager(activity!!)
        itemToShow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                fileAdapter.setItem(it.files)
            }
        })

        val eventAdapter = EventListAdapter(activity!!)
        rv_events.adapter = eventAdapter
        rv_events.layoutManager = LinearLayoutManager(activity!!)
        itemToShow.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                eventAdapter.setItem(it.events)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).title = getString(R.string.show_details)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.button_delete)
        {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.setMessage(getString(R.string.confirm_delete))?.setPositiveButton(getString(R.string.yes)) {_, _ ->
                AsyncTask.execute {
                    AppDatabase.getDatabase(activity!!).itemDao().delete(this.args.ID)
                }
                view!!.findNavController().popBackStack()
            }
                ?.setNegativeButton(getString(R.string.no)) { _, _ -> }?.show()
        }
        return super.onOptionsItemSelected(item)
    }
}