package sk.tuke.archivator.RoomComponents

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.R
import sk.tuke.archivator.ViewModels.ItemViewModel

class ImageListAdapter(context: Context): RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var images = emptyList<Uri>() // Cached copy of items
    private var item = Item()
    private var model: ItemViewModel? = null

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.iv_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = inflater.inflate(R.layout.image, parent, false)
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val current = images[position]
        holder.itemImage.setImageURI(current)

        if (model != null)
        {
            holder.itemView.setOnClickListener {
                item.images.remove(current)
                model!!.tmpItem.postValue(model!!.tmpItem.value)
            }
        }
    }

    internal fun setItem(item: Item, viewModel: ItemViewModel? = null ) {
        this.item = item
        this.images = item.images
        this.model = viewModel
        notifyDataSetChanged()
    }

    override fun getItemCount() = images.size
}