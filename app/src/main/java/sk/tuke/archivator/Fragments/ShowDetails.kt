package sk.tuke.archivator.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_show_details.*
import sk.tuke.archivator.R
import sk.tuke.archivator.ViewModels.ItemViewModel

/**
 * A simple [Fragment] subclass.
 */
class ShowDetails : Fragment() {

    val args: ShowDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_details, container, false)
    }

    override fun onStart() {
        super.onStart()

        val amount = args.ID
        tv_id.text = "Item id passed is ${amount}"

        val itemViewModel = activity?.run {
            ViewModelProviders.of(this)[ItemViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        itemViewModel.itemDao.getOneById(args.ID).observe(this, Observer {
            it.let {
                tv_name.text = "Name ${it.name}"
                tv_desc.text = "Description:\n  ${it.desc}"
            }
        })
    }
}