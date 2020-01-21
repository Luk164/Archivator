package sk.tuke.archivator.RoomComponents

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.tuke.archivator.Entities.ItemWithStuff
import sk.tuke.archivator.Objects.NewItem
import sk.tuke.archivator.R
import sk.tuke.archivator.ViewModels.ItemViewModel

class SingleItemContentListAdapter(context: Context): RecyclerView.Adapter<SingleItemContentListAdapter.ImageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var images: List<Uri>// Cached copy of items
    private lateinit var item: ItemWithStuff
    private var model: ItemViewModel? = null

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.iv_image)
        val itemDesc: TextView = itemView.findViewById(R.id.tv_desc)
        val itemDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = inflater.inflate(R.layout.item_content, parent, false)
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val current = images[position]
        holder.itemImage.setImageURI(current)

        if (model != null)
        {
//            holder.itemView.setOnClickListener {
//                item.images.remove(current)
//                NewItem.tmpItem.postValue(
//                    NewItem.tmpItem.value)
//            }
        }
    }

    internal fun setItem(item: ItemWithStuff, viewModel: ItemViewModel? = null ) {
        this.item = item
        this.model = viewModel
        notifyDataSetChanged()
    }

    override fun getItemCount() = images.size
}