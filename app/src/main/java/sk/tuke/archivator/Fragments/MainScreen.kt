package sk.tuke.archivator.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_main_screen.*
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Global
import sk.tuke.archivator.R

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
//        dbOutput.text = Global.db.itemDao().getAllLive()
        val observer = Observer<List<Item>> {
            if (it.isNotEmpty())
            {
                dbOutput.text = "Success${it[0].name}"
            }
        }

        Global.db.itemDao().getAllLive().observe(this, observer)
    }
}
