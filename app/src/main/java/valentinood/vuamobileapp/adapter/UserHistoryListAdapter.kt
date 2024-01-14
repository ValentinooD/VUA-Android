package valentinood.vuamobileapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import valentinood.vuamobileapp.R
import valentinood.vuamobileapp.model.User
import valentinood.vuamobileapp.sheets.ViewSavedUserBottomSheet

class UserHistoryListAdapter(
    private val items: MutableList<User>,
    private val context: Context,
    private val listener: (user: User) -> Unit = {})
    : RecyclerView.Adapter<UserHistoryListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPhoto = itemView.findViewById<ImageView>(R.id.iv_photo);
        private val tvFullName = itemView.findViewById<TextView>(R.id.tv_fullname);
        private val tvEmail = itemView.findViewById<TextView>(R.id.tv_email)
        private val container = itemView.findViewById<ViewGroup>(R.id.ll_container)

        fun bind(user: User) {
            ivPhoto.setImageDrawable(user.getPicture(context))
            tvFullName.text = user.fullName
            tvEmail.text = user.email

            container.setOnClickListener {
                val sheet = ViewSavedUserBottomSheet(context, user)
                sheet.show()

                sheet.setOnDismissListener {
                    if (sheet.useThisUser) {
                        listener(user)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}