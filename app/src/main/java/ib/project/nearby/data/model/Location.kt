package ib.project.nearby.data.model

data class Location(
    val cc: String? = null,
    val country: String? = null,
    val address: String? = null,
    val labeledLatLngs: List<LabeledLatLngsItem?>? = null,
    val lng: Double? = null,
    val distance: Int? = null,
    val formattedAddress: List<String?>? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val state: String? = null,
    val lat: Double? = null
)
