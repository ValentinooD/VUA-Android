package valentinood.vuamobileapp.sheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import valentinood.vuamobileapp.R
import valentinood.vuamobileapp.databinding.SheetViewEmailBinding
import valentinood.vuamobileapp.model.Message

class ViewEmailBottomSheet(private val context: Context, private val message: Message) : BottomSheetDialog(context) {
    private lateinit var binding: SheetViewEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SheetViewEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvFrom.text = context.getString(R.string.from_email, message.from)
        binding.tvSubject.text = message.subject

        binding.wvHtml.loadData(message.html, "text/html", "UTF-8")
    }
}