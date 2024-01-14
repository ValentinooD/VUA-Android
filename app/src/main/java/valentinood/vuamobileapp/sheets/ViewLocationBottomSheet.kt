package valentinood.vuamobileapp.sheets

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import valentinood.vuamobileapp.databinding.SheetViewLocationBinding
import valentinood.vuamobileapp.framework.callDelayed
import valentinood.vuamobileapp.model.User


class ViewLocationBottomSheet(ctx: Context, private val user: User) : BottomSheetDialog(ctx) {
    private lateinit var binding: SheetViewLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SheetViewLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCountry.text = user.country
        binding.ivFlag.setImageDrawable(user.getCountryFlag(context)?.getImage(context))

        binding.mvMap.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }

        binding.mvMap.controller.setZoom(6.0)
        binding.mvMap.minZoomLevel = 3.0
        binding.mvMap.controller.setCenter(GeoPoint(0.0, 0.0))

        callDelayed(500L) {
            if (!isShowing) return@callDelayed

            val startMarker = Marker(binding.mvMap)
            startMarker.position = user.geoPoint
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            binding.mvMap.overlays.add(startMarker)

            binding.mvMap.controller.setCenter(user.geoPoint)
        }
    }
}