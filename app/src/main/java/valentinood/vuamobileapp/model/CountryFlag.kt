package valentinood.vuamobileapp.model

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

data class CountryFlag(
    val _id: Long?,
    val nat: String,
    val fileHash: String,
    val filePath: String
) {
    private var image: Drawable? = null

    fun getImage(context: Context): Drawable {
        if (image != null) return image!!

        image = BitmapDrawable(context.resources, filePath)
        return image!!
    }
}