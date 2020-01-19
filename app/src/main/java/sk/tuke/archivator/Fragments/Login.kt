package sk.tuke.archivator.Fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import sk.tuke.archivator.MainActivity
import sk.tuke.archivator.R
import sk.tuke.archivator.ViewModels.AppViewModel

/**
 * A simple [Fragment] subclass.
 */
class Login : Fragment() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        appViewModel = activity?.run {
            ViewModelProviders.of(this)[AppViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        view.button_login.setOnClickListener{
            appViewModel.username.value = et_email.text.toString()
            view?.findNavController()?.navigate(R.id.action_login_to_mainScreen)

            (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).title = getString(R.string.login)
    }
}
