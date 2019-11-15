package sk.tuke.archivator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_main_screen.view.*

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

        view.button_settings.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainScreen_to_settings2)
        }

        return view
    }


}
