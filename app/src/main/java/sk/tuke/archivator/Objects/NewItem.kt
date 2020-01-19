package sk.tuke.archivator.Objects

import androidx.lifecycle.MutableLiveData
import sk.tuke.archivator.Entities.Event
import sk.tuke.archivator.Entities.FileEntity
import sk.tuke.archivator.Entities.Image
import sk.tuke.archivator.Entities.Item
import java.io.File

object NewItem {
    val tmpItem: MutableLiveData<Item> by lazy {
        MutableLiveData<Item>().apply {
            this.value = Item()
        }
    }

    val tmpEvents: MutableLiveData<List<Event>> = MutableLiveData<List<Event>>().apply { this.value = mutableListOf() }

    val tmpImages: MutableLiveData<List<Image>> = MutableLiveData<List<Image>>().apply { this.value = mutableListOf() }

    val tmpFiles: MutableLiveData<List<FileEntity>> = MutableLiveData<List<FileEntity>>().apply { this.value = mutableListOf() }

    /**
     * Clears temporary item and its contents
     */
    fun clear()
    {
        tmpItem.value = Item()
        tmpEvents.value = mutableListOf()
        tmpImages.value = mutableListOf()
        tmpFiles.value = mutableListOf()
    }
}