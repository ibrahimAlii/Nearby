package ib.project.nearby.viewmodels

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.await
import ib.project.nearby.data.Place
import ib.project.nearby.data.PlaceRepository
import ib.project.nearby.utilities.LocationUtils
import ib.project.nearby.workers.NearbyWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NearbyViewModel internal constructor(placeRepository: PlaceRepository) : ViewModel(), Observable {

    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    private fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }
    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    @Suppress("unused")
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     *
     *   Load using default mode ---> if changed to realtime ---> run continuously if move more than 500m ---> if back to default mode cancel.current
     *
     */
    private val TAG = NearbyViewModel::class.java.simpleName
    private val loadMode = MutableLiveData<Boolean>(false)
    private val lastFireLocation = MutableLiveData<Pair<Double, Double>>()



    var emptyVisibility : Boolean = true
        @Bindable get() = field
        set(value)  {
            field = value
            notifyPropertyChanged(BR._all)
        }


    var errorVisibility : Boolean = true
    @Bindable get() = field
    set(value)  {
        field = value
        notifyPropertyChanged(BR._all)
    }



    init {
        lastFireLocation.value = Pair(0.0, 0.0)
    }

    val places: LiveData<List<Place>> = loadMode.switchMap {
        viewModelScope.runCatching {
            if (it) {
                viewModelScope.launch {
                    runContinuously()
                }
                placeRepository.getPlaces()

            } else {
                viewModelScope.launch {
                    runNearbyWorker()
                }
                placeRepository.getPlaces()

            }
        }.getOrDefault(MutableLiveData<List<Place>>())


    }


    fun realTimeMode() {
        loadMode.value = true
    }

    fun defaultMode() {
        loadMode.value = false
    }

    fun isRealTime() = loadMode.value != false


    private suspend fun runContinuously() {
        val currentLocation = getCurrentLocationAsPair()
        while (isRealTime()) {
            delay(300)
            //Log.i(TAG, "I'm Running continuously every 300ms")
            if (LocationUtils.distFrom(
                            currentLocation.first, currentLocation.second,
                            lastFireLocation.value!!.first, lastFireLocation.value!!.second
                    ) > 500
            ) {
                runNearbyWorker()
                lastFireLocation.value = getCurrentLocationAsPair()
            }
        }
    }

    private fun getCurrentLocationAsPair(): Pair<Double, Double> {
        val location = LocationUtils.lastSaveLocation!!.split(",")
        return Pair(location[0].toDouble(), location[1].toDouble())
    }


    private suspend fun runNearbyWorker() : Operation.State {
        val request = OneTimeWorkRequestBuilder<NearbyWorker>().build()
        WorkManager.getInstance().getWorkInfoByIdLiveData(request.id).observeForever { workInfo ->
            if (workInfo.state.isFinished) {
                if (workInfo.outputData.getInt(NearbyWorker.STATE, 0) == NearbyWorker.SUCCESS) {
                    errorVisibility = true

                } else if (workInfo.outputData.getInt(NearbyWorker.STATE, 0) == NearbyWorker.FAIL) {
                    errorVisibility = false
                }
            }
        }
        return WorkManager.getInstance().enqueue(request).await()
    }


}
