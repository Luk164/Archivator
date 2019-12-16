package sk.tuke.archivator.RoomComponents

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.Fragments.MainScreenDirections
import sk.tuke.archivator.R

//class PartAdapter (val partItemList: List<PartData>, val clickListener: (PartData) -> Unit) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>()

class ItemListAdapter(context: Context) : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var items = emptyList<Item>() // Cached copy of items

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemId: TextView = itemView.findViewById(R.id.tv_item_id)
        val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
        val itemDesc: TextView = itemView.findViewById(R.id.tv_item_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = items[position]
        holder.itemId.text = current.id.toString()
        holder.itemName.text = current.name
        holder.itemDesc.text = current.desc
        holder.itemView.setOnClickListener {
            val action = MainScreenDirections.actionMainScreenToShowDetails(current.id)
            it.findNavController().navigate(action)
        }
    }

    internal fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
//        this.items.remo
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, imageModelArrayList.size)
        Log.e("TEST", "SUCCESS")
    }

    override fun getItemCount() = items.size
}