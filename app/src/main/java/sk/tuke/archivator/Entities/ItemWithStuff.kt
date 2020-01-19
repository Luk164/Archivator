package sk.tuke.archivator.Entities

import androidx.room.Embedded
import androidx.room.Relation
import java.io.File

data class ItemWithStuff(
    @Embedded
    val item: Item,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentItemId"
    )
    val events: List<Event>,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentItemId"
    )
    val images: List<Image>,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentItemId"
    )
    val files: List<FileEntity>
)