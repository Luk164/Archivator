package sk.tuke.archivator.RoomComponents

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.tuke.archivator.Entities.Item
import sk.tuke.archivator.R

class ItemListAdapter(
    context: Context
) : RecyclerView.Adapter<ItemListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<Item>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemId: TextView = itemView.findViewById(R.id.tv_item_id)
        val itemName: TextView = itemView.findViewById(R.id.tv_item_name)
        val itemDesc: TextView = itemView.findViewById(R.id.tv_item_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        holder.itemId.text = current.id.toString()
        holder.itemName.text = current.name
        holder.itemDesc.text = current.desc
    }

    internal fun setWords(words: List<Item>) {
        this.words = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = words.size
}