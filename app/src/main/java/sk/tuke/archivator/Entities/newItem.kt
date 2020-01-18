package sk.tuke.archivator.Entities

import androidx.lifecycle.MutableLiveData

object newItem {
    val tmpItem: MutableLiveData<Item> by lazy {
        MutableLiveData<Item>().apply {
            this.value = Item()
        }
    }

    val tmpEvents: MutableLiveData<List<Event>> by lazy {
        MutableLiveData<List<Event>>().apply {
            this.value = mutableListOf()
        }
    }

    /**
     * Clears temporary item and its contents
     */
    fun clear()
    {
        this.tmpItem.value = Item()
        this.tmpEvents.value = mutableListOf()
    }
}