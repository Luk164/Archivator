package sk.tuke.archivator.RoomComponents

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
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
import sk.tuke.archivator.Entities.Image
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Objects.NewItem
import sk.tuke.archivator.R
import java.lang.Exception

class PictureListAdapter(context: Context): RecyclerView.Adapter<PictureListAdapter.ImageViewHolder>() {

    private val context = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var images = emptyList<Image>() // Cached copy of items

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
        val current = images[position]
        holder.image.setImageURI(current.uri)
        holder.desc.text = current.description

        holder.desc.setOnClickListener {
            val etName = EditText(context)
            AlertDialog.Builder(context).apply {
                this.setTitle("Description")
                this.setMessage("Set new description")
                this.setView(etName)
                this.setPositiveButton("Add") { _, _ ->
                    holder.desc.text = etName.text.toString() //update view
                    current.description = etName.text.toString() //update image
                }
                this.setNegativeButton("Cancel", null)
            }.create().show()
        }

        holder.btDelete.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                AppDatabase.getDatabase(context).imageDao().delete(current.id)

                NewItem.tmpItem.postValue(
                    NewItem.tmpItem.value)
            }
            (it.parent.parent as ViewGroup).removeView(it.parent as View) //self terminate
        }
    }

    internal fun setItem(images: List<Image>) {
        this.images = images

        notifyDataSetChanged()
    }

    override fun getItemCount() = images.size
}