package ib.project.nearby.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "place")
data class Place(
    @PrimaryKey @ColumnInfo(name = "id") val placeId: String,
    val name: String,
    val description: String,
    val image: String
)
