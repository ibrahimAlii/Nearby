package ib.project.nearby

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.ProgressBar
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.get
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import ib.project.nearby.adapters.PlacesAdapter
import ib.project.nearby.databinding.NearbyFragmentBinding
import ib.project.nearby.utilities.InjectorUtils
import ib.project.nearby.utilities.LocationUtils
import ib.project.nearby.viewmodels.NearbyViewModel


class NearbyFragment : Fragment() {

    val TAG = NearbyFragment::class.java.simpleName

    private val viewModel: NearbyViewModel by viewModels {
        InjectorUtils.provideNearbyViewModelFactory(requireContext())
    }

    private var progress: ProgressBar? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = NearbyFragmentBinding.inflate(inflater, container, false).apply {
            model = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        context ?: return binding.root

        setHasOptionsMenu(true)

        val adapter = PlacesAdapter()
        binding.placeList.adapter = adapter
        subscribeUi(adapter)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        progress = menu[1].actionView as ProgressBar
        progress?.visibility
        val switcher = menu[0].actionView as SwitchCompat
        switcher.showText = true
        switcher.textOn = "RealTime"
        switcher.textOff = "Default"
        switcher.setOnClickListener { updateData() }
    }

    private fun subscribeUi(adapter: PlacesAdapter) {
        viewModel.places.observe(viewLifecycleOwner) { plants ->
            viewModel.emptyVisibility = plants.isNotEmpty()
            progress?.visibility = GONE
            adapter.submitList(plants)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun updateData() {
        with(viewModel) {
            if (isRealTime()) {
                defaultMode()
            } else {
                realTimeMode()
            }
        }
    }

}
