package sk.tuke.archivator.Fragments


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_show_details.*
import sk.tuke.archivator.MainActivity
import sk.tuke.archivator.R
import sk.tuke.archivator.RoomComponents.AppDatabase
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_details, container, false)
    }

    //TODO FIXME
    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        val value = args.ID
        tv_id.text = "${getString(R.string.item_id_is)}$value"

        val itemViewModel = activity?.run {
            ViewModelProviders.of(this)[ItemViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        itemViewModel.itemDao.getOneById(args.ID).observe(this, Observer {
            it.let {
                tv_name.text = "${getString(R.string.name)} ${it.name}"
                tv_desc.text = "${getString(R.string.description)}\n  ${it.desc}"
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