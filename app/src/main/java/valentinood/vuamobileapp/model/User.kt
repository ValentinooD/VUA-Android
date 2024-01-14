package valentinood.vuamobileapp.model

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import org.osmdroid.util.GeoPoint
import valentinood.vuamobileapp.framework.getCountryFlag
import java.io.Serializable


data class User(
    val _id: Long? = null,
    val fullName: String,
    val email: String,
    val country: String,
    val nationality: String,
    val picturePath: String,
    val latitude: Double,
    val longitude: Double,
    var geoPoint: GeoPoint? = null
) : Serializable {
    @Transient
    private var image: Drawable? = null

    init {
        geoPoint = GeoPoint(latitude, longitude)
    }

    fun getPicture(context: Context): Drawable {
        if (image != null) return image!!

        image = BitmapDrawable(context.resources, picturePath)
        return image!!
    }

    fun getCountryFlag(context: Context): CountryFlag? {
        return context.getCountryFlag(nationality)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (_id != other._id) return false
        if (fullName != other.fullName) return false
        if (email != other.email) return false
        if (country != other.country) return false
        if (nationality != other.nationality) return false
        if (picturePath != other.picturePath) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (geoPoint != other.geoPoint) return false
        return image == other.image
    }

    override fun hashCode(): Int {
        var result = _id?.hashCode() ?: 0
        result = 31 * result + fullName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + nationality.hashCode()
        result = 31 * result + picturePath.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + (geoPoint?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        return result
    }
}
