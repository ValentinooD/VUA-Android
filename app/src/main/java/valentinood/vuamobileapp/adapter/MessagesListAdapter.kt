package valentinood.vuamobileapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import valentinood.vuamobileapp.R
import valentinood.vuamobileapp.model.Message
import valentinood.vuamobileapp.sheets.ViewEmailBottomSheet

class MessagesListAdapter(private val items: MutableList<Message>, private val context: Context)
    : RecyclerView.Adapter<MessagesListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFrom = itemView.findViewById<TextView>(R.id.tv_from);
        private val tvSubject = itemView.findViewById<TextView>(R.id.tv_subject);
        private val tvText = itemView.findViewById<TextView>(R.id.tv_text)
        private val container = itemView.findViewById<ViewGroup>(R.id.ll_container)

        fun bind(message: Message) {
            tvFrom.text = message.from
            tvSubject.text = message.subject
            tvText.text = message.text

            container.setOnClickListener {
                val dialog = ViewEmailBottomSheet(context, message)
                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}