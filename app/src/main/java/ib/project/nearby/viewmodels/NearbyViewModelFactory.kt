package ib.project.nearby.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ib.project.nearby.data.PlaceRepository

class NearbyViewModelFactory (
    private val repository: PlaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = NearbyViewModel(repository) as T
}