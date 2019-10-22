package ib.project.nearby.utilities

import android.content.Context
import ib.project.nearby.data.AppDatabase
import ib.project.nearby.data.PlaceRepository
import ib.project.nearby.viewmodels.NearbyViewModelFactory

object InjectorUtils {


    private fun getPlaceRepository(context: Context): PlaceRepository {
        return PlaceRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).placeDao()
        )
    }

    fun provideNearbyViewModelFactory(
        context: Context
    ): NearbyViewModelFactory {
        val repository = getPlaceRepository(context)
        return NearbyViewModelFactory(repository)
    }


}
