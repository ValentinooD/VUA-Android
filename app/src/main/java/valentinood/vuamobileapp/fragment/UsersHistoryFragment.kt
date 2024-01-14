package valentinood.vuamobileapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import valentinood.vuamobileapp.R
import valentinood.vuamobileapp.adapter.UserHistoryListAdapter
import valentinood.vuamobileapp.databinding.FragmentUsersHistoryBinding
import valentinood.vuamobileapp.framework.callDelayed
import valentinood.vuamobileapp.framework.getUsers
import valentinood.vuamobileapp.model.User
import valentinood.vuamobileapp.provider.HISTORY_PROVIDER_CONTENT_URI

class UsersHistoryFragment(
    private val userChangedListener: (user: User) -> Unit = {}
) : Fragment() {
    private lateinit var binding: FragmentUsersHistoryBinding
    private lateinit var historyAdapter: UserHistoryListAdapter

    private var list: MutableList<User> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUsersHistoryBinding.inflate(layoutInflater)

        setupUI()

        val touchHelper = ItemTouchHelper(UserCardSwipeCallback())
        touchHelper.attachToRecyclerView(binding.rvHistory)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        setLoading(true)

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            list.clear()
            list.addAll(requireContext().getUsers())

            // delay is to sync with UI
            callDelayed(1L) {
                historyAdapter.notifyDataSetChanged()
                setLoading(false)
            }
        }
    }

    private fun setupUI() {
        setLoading(true)

        historyAdapter = UserHistoryListAdapter(list, requireContext(), userChangedListener)
        binding.rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLabel() {
        if (list.isEmpty()) {
            binding.rvHistory.visibility = View.GONE
            binding.tvNoHistory.visibility = View.VISIBLE
        } else {
            binding.rvHistory.visibility = View.VISIBLE
            binding.tvNoHistory.visibility = View.GONE
        }
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            binding.llContent.visibility = View.GONE
            binding.llLoading.visibility = View.VISIBLE
        } else {
            binding.llContent.visibility = View.VISIBLE
            binding.llLoading.visibility = View.GONE

            // only called when llContent is set to visible
            showLabel()
        }
    }

    inner class UserCardSwipeCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val user = list.elementAt(viewHolder.adapterPosition)

            list.removeAt(viewHolder.adapterPosition)
            historyAdapter.notifyItemRemoved(viewHolder.adapterPosition)

            context?.contentResolver?.delete(
                HISTORY_PROVIDER_CONTENT_URI.buildUpon().appendPath(user._id.toString()).build(),
                null, null
            )

            Snackbar.make(binding.llContent, getString(R.string.user_deleted), Snackbar.LENGTH_SHORT).show()
        }
    }
}