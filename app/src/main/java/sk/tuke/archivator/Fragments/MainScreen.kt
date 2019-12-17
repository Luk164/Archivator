package sk.tuke.archivator.Fragments


import android.graphics.*
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main_screen.*
import kotlinx.android.synthetic.main.fragment_main_screen.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    val p : Paint  = Paint()
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

        itemViewModel.itemDao.getAllLive().observe(this, Observer {
            it?.let {
                adapter.setItems(it) }
        })
        initSwipe()
    }

    private fun initSwipe() {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {

                    //Logic to do when swipe left
                    Toast.makeText(activity!!, "Swipe left (DELETE) on ID: ${(viewHolder as ItemListAdapter.ItemViewHolder).itemId.text}", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.Default).launch {
                        AppDatabase.getDatabase(activity!!).itemDao().delete(viewHolder.itemId.text.toString().toInt())
                    }

                } else {

                    //Logic to do when swipe right
                    Toast.makeText(activity!!, "Swipe right (UPLOAD) on ID: ${(viewHolder as ItemListAdapter.ItemViewHolder).itemId.text}", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.Default).launch {
                        Global.VNM.sendItem(AppDatabase.getDatabase(activity!!).itemDao().getOneById(viewHolder.itemId.text.toString().toInt()))
                    }
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    Log.i("dX", dX.toString())
                    if (dX > 0) {
                        //Drawing for Swipe Right
                        p.color = Color.parseColor("#2F2FD3")
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            resources.getDrawable(R.drawable.archive_black_24dp, activity?.theme)
                        } else {
                            @Suppress("DEPRECATION")
                            resources.getDrawable(R.drawable.archive_black_24dp)
                        }.apply {
                            this.setBounds(
                                itemView.left,
                                itemView.top,
                                (itemView.left + 2 * width).toInt(),
                                itemView.bottom
                            )
                            this.draw(c)
                        }

                    } else {

                        //Drawing for Swipe Left

                        p.color = Color.parseColor("#D32F2F")
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            resources.getDrawable(R.drawable.delete_sweep_black_24dp, activity?.theme)
                        } else {
                            @Suppress("DEPRECATION")
                            resources.getDrawable(R.drawable.delete_sweep_black_24dp)
                        }.apply {
                            this.setBounds(
                                (itemView.right - 2 * width).toInt(),
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                            )
                            this.draw(c)
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(simpleItemTouchCallback).apply {
            this.attachToRecyclerView(rw_items) }
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
