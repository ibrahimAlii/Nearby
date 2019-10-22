package ib.project.nearby.data

/**
 * Repository module for handling data operations.
 */
class PlaceRepository private constructor(private val placeDao: PlaceDao) {

    fun getPlaces() = placeDao.getPlaces()

    fun getPlace(placeId: String) = placeDao.getPlace(placeId)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PlaceRepository? = null

        fun getInstance(placeDao: PlaceDao) =
            instance ?: synchronized(this) {
                instance ?: PlaceRepository(placeDao).also { instance = it }
            }
    }
}