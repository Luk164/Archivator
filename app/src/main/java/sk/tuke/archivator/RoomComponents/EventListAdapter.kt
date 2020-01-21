package sk.tuke.archivator.RoomComponents

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.tuke.archivator.Entities.Event
import sk.tuke.archivator.Entities.FileEntity
import sk.tuke.archivator.Objects.NewItem
import sk.tuke.archivator.R

class EventListAdapter(context: Context): RecyclerView.Adapter<EventListAdapter.ImageViewHolder>() {

    private val context = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var events = emptyList<Event>() // Cached copy of items

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_image)
        val desc: TextView = itemView.findViewById(R.id.tv_desc)
//        val date: TextView = itemView.findViewById(R.id.tv_date)
        val btDelete: Button = itemView.findViewById(R.id.bt_delete_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(inflater.inflate(R.layout.item_content, parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val current = events[position]
        holder.image.setImageResource(R.drawable.ic_attach_file_black_24dp)
        holder.desc.text = current.name

        holder.desc.setOnClickListener {
            val etName = EditText(context).apply {
                this.setText(current.name)
            }
            AlertDialog.Builder(context).apply {
                this.setTitle("Rename")
                this.setMessage("Set new name")
                this.setView(etName)
                this.setPositiveButton("Rename") { _, _ ->
                    holder.desc.text = etName.text.toString() //update view
                    current.name = etName.text.toString() //update image
                }
                this.setNegativeButton("Cancel", null)
            }.create().show()
        }

        holder.btDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                AppDatabase.getDatabase(context).eventDao().delete(current.id)

                NewItem.tmpEvents.postValue(
                    NewItem.tmpEvents.value)
            }
            (it.parent.parent as ViewGroup).removeView(it.parent as View) //self terminate
        }
    }

    internal fun setItem(events: List<Event>) {
        this.events = events

        notifyDataSetChanged()
    }

    override fun getItemCount() = events.size
}