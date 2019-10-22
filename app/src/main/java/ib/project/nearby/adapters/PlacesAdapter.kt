package ib.project.nearby.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ib.project.nearby.data.Place
import ib.project.nearby.databinding.ListItemNearbyBinding

class PlacesAdapter : ListAdapter<Place, RecyclerView.ViewHolder>(PlaceDiffCallback()) {

    private val TAG = PlacesAdapter::class.java.simpleName
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val place = getItem(position)
        (holder as PlaceViewHolder).bind(place)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaceViewHolder(
            ListItemNearbyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class PlaceViewHolder(
        private val binding: ListItemNearbyBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.place?.let { place ->
                    navigateToPlant(place, it)
                }
            }
        }

        private fun navigateToPlant(
            place: Place,
            it: View
        ) {
            // navigate to some places
        }

        fun bind(item: Place) {
            binding.apply {
                place = item
                executePendingBindings()
            }
        }
    }
}

private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}