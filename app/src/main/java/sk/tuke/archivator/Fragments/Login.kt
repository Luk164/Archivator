package sk.tuke.archivator.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import sk.tuke.archivator.R
import sk.tuke.archivator.VolleyNetworkManager

/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.button_login.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_login_to_mainScreen)
        }

        return view
    }
}
