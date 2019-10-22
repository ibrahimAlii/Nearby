package ib.project.nearby.utilities


import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import ib.project.nearby.R
import java.text.DateFormat
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


internal object LocationUtils {

    private const val NAME = "LEARN_ISLAM.APP"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    val KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates"
    val KEY_LOCATION = "last_saved_location"


    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            NAME,
            MODE
        )
    }


    private inline fun SharedPreferences.edit(
        operation:
            (SharedPreferences.Editor) -> Unit
    ) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var lastSaveLocation: String?
        get() = preferences.getString(KEY_LOCATION, "0000,0000")
        set(value) = preferences.edit {
            it.putString(KEY_LOCATION, value)
        }

    var locationRequestUpdate: Boolean
        get() = preferences.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
        set(value) = preferences.edit {
            it.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, value)
        }


    /**
     * Returns the `location` object as a human readable string.
     * @param location  The [Location].
     */
    fun getLocationText(location: Location?): String {
        val text = location?.latitude?.toString()?.plus(",")?.plus(location.longitude)
            ?: "Unknown location"
        lastSaveLocation = text
        return text
    }

    fun getLocationTitle(context: Context): String {
        return context.getString(
            R.string.location_updated,
            DateFormat.getDateTimeInstance().format(Date())
        )
    }

    fun distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians((lat2 - lat1))
        val dLng = Math.toRadians((lng2 - lng1))
        val a =
            sin(dLat / 2) * sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * cos(
                Math.toRadians(lat2)
            ) *
                    Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), Math.sqrt(1 - a))

        return (earthRadius * c)
    }
}
