package sk.tuke.archivator.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main_screen.*
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import sk.tuke.archivator.R
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
}
