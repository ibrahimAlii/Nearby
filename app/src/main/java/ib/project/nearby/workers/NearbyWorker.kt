package ib.project.nearby.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ib.project.nearby.data.AppDatabase
import ib.project.nearby.data.Place
import ib.project.nearby.data.model.Data
import ib.project.nearby.network.Client
import ib.project.nearby.network.NearbyAPI
import ib.project.nearby.utilities.CLIENT_ID
import ib.project.nearby.utilities.CLIENT_SECRET
import ib.project.nearby.utilities.LocationUtils
import kotlinx.coroutines.coroutineScope

class NearbyWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        val database = AppDatabase.getInstance(applicationContext)
        val data = androidx.work.Data.Builder()
        try {
            database.placeDao().insertAll(getPlacesFromLegacyResponse())

            data.putInt(STATE, SUCCESS)
            data.putString(MESSAGE, "Success call")
            Result.success(data.build())
        } catch (ex: Exception) {
            Log.e(TAG, "Error database", ex)
            database.placeDao().insertAll(emptyList())

            data.putInt(STATE, FAIL)
            data.putString(MESSAGE, ex.localizedMessage)
            Result.failure(data.build())
        }
    }


    private suspend fun getPlacesData(): Data {
        return coroutineScope {
            Client.retrofit.create(NearbyAPI::class.java).getNearbyPlacesAsync(
                CLIENT_ID, CLIENT_SECRET,
                LocationUtils.lastSaveLocation!!,
                "20191010"
            )
        }.await()

    }

    private suspend fun getPlacesFromLegacyResponse(): List<Place> {
        val places = ArrayList<Place>()
        val items = getPlacesData().response!!.groups!![0]!!.items

        for (item in items!!) {
            val venue = item!!.venue!!
            val category = venue.categories?.get(0)!!
            val place = Place(
                venue.id!!, venue.name!!, category.name!!,
                category.icon!!.prefix!!.plus(category.icon.suffix)
            )
            places.add(place)
        }

        Log.i(TAG, places.toString())
        return places
    }


    companion object {
        private val TAG = NearbyWorker::class.java.simpleName
        const val STATE = "state"
        const val SUCCESS = 200;
        const val FAIL = 401;
        const val MESSAGE = "message";
    }
}