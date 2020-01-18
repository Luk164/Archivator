package sk.tuke.archivator.Entities

import androidx.room.Embedded
import androidx.room.Relation

data class ItemWithStuff(
    @Embedded
    val item: Item,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentItemId"
    )
    val events: List<Event>
)