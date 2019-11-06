package sk.tuke.archivator


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        button_login.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_login_to_mainScreen)
        }

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onResume() {
        super.onResume()
    }


}
