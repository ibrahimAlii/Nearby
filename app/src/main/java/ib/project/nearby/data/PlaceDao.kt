package ib.project.nearby.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Query("SELECT * FROM place ORDER BY name")
    fun getPlaces(): LiveData<List<Place>>

    @Query("SELECT * FROM place WHERE id = :placeId")
    fun getPlace(placeId: String): LiveData<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(places: List<Place>)
}