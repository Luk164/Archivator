package sk.tuke.archivator.Fragments


import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main_screen.*
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Global
import sk.tuke.archivator.MainActivity
import sk.tuke.archivator.R
import sk.tuke.archivator.RoomComponents.AppDatabase
import sk.tuke.archivator.RoomComponents.ItemListAdapter
import sk.tuke.archivator.ViewModels.ItemViewModel

/**
 * A simple [Fragment] subclass.
 */
class MainScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_main_screen, container, false)
        setHasOptionsMenu(true)
        view.button_newEntry.setOnClickListener{
            view.findNavController().navigate(R.id.action_mainScreen_to_newEntry)
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val adapter = ItemListAdapter(activity!!)
        rw_items.adapter = adapter
        rw_items.layoutManager = LinearLayoutManager(activity!!)

        val itemViewModel = activity?.run {
                        ViewModelProviders.of(this)[ItemViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        itemViewModel.itemDao.getAll().observe(this, Observer { items ->
            items?.let { adapter.setItems(it) }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).title = getString(R.string.main_screen)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.upload_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.button_upload)
        {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.setMessage(getString(R.string.confirm_upload))?.setPositiveButton(getString(R.string.upload)) {_, _ ->

                AsyncTask.execute {
                    val list = AppDatabase.getDatabase(activity!!).itemDao().getAllSync()
                    AppDatabase.getDatabase(activity!!).itemDao().getAllSync().forEach {
                        Global.VNM.sendItem(it)
                    }
                }
            }
                ?.setNegativeButton(getString(R.string.cancel)) { _, _ -> }?.show()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}
