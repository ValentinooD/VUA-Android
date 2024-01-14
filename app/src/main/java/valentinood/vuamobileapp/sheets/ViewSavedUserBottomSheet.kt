package valentinood.vuamobileapp.sheets

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import valentinood.vuamobileapp.databinding.SheetViewSavedUserBinding
import valentinood.vuamobileapp.model.User

class ViewSavedUserBottomSheet(private val context: Context, val user: User) : BottomSheetDialog(context) {
    private lateinit var binding: SheetViewSavedUserBinding

    var useThisUser: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SheetViewSavedUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvFullname.text = user.fullName
        binding.tvEmail.text = user.email
        binding.ivPhoto.setImageDrawable(user.getPicture(context))

        binding.btnUse.setOnClickListener {
            useThisUser = true
            dismiss()
        }
    }
}