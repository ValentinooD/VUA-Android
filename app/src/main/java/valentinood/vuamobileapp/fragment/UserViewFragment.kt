package valentinood.vuamobileapp.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import valentinood.vuamobileapp.R
import valentinood.vuamobileapp.SplashScreenActivity
import valentinood.vuamobileapp.adapter.MessagesListAdapter
import valentinood.vuamobileapp.api.email.EmailFetcher
import valentinood.vuamobileapp.databinding.FragmentUserViewBinding
import valentinood.vuamobileapp.framework.callDelayed
import valentinood.vuamobileapp.framework.getUser
import valentinood.vuamobileapp.framework.startActivity
import valentinood.vuamobileapp.model.Message
import valentinood.vuamobileapp.model.User
import valentinood.vuamobileapp.provider.HISTORY_PROVIDER_CONTENT_URI
import valentinood.vuamobileapp.sheets.ViewLocationBottomSheet
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask

const val INTENT_USER = "valentinood.vuamobileapp.fragment.UserViewFragment.USER"
class UserViewFragment(private val intent: Intent) : Fragment() {
    private lateinit var user: User
    private lateinit var binding: FragmentUserViewBinding
    private lateinit var messagesAdapter: MessagesListAdapter
    private lateinit var timer: Timer
    private lateinit var task: TimerTask

    private var userBookmarked = false
    private var list: MutableList<Message> = mutableListOf()

    @Suppress("deprecated")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserViewBinding.inflate(layoutInflater)

        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(INTENT_USER, User::class.java)!!
        } else {
            intent.getSerializableExtra(INTENT_USER) as User
        }

        userBookmarked = user._id != null

        setupUI()
        setupEvents()

        return binding.root
    }

    private fun bookmarkUser() {
        userBookmarked = !userBookmarked

        try {
            if (userBookmarked) {
                val uri = context?.contentResolver?.insert(
                    HISTORY_PROVIDER_CONTENT_URI,
                    ContentValues().apply {
                        put(User::fullName.name, user.fullName)
                        put(User::email.name, user.email)
                        put(User::country.name, user.country)
                        put(User::nationality.name, user.nationality)
                        put(User::picturePath.name, user.picturePath)
                        put(User::latitude.name, user.latitude)
                        put(User::longitude.name, user.longitude)
                    }
                )

                user = requireContext().getUser(uri!!.lastPathSegment!!.toLong())!!
                setUser(user)
            } else {
                context?.contentResolver?.delete(
                    HISTORY_PROVIDER_CONTENT_URI.buildUpon().appendPath(user._id.toString()).build(),
                    null, null
                )
            }

            setupUI()
        } catch (e: Exception) {
            binding.ivSave.setImageResource(R.drawable.error)
                Snackbar.make(binding.root, "Unable to bookmark", Snackbar.LENGTH_LONG).show()

            Log.e("UserViewFragment", "Failed to bookmark user", e)
        }
    }

    private fun refresh() {
        callDelayed(250) {
            context?.startActivity<SplashScreenActivity>()
            activity?.finish()
        }
    }

    override fun onResume() {
        super.onResume()

        setupUI()
        startTimer()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    private fun startTimer() {
        timer = Timer()
        task = timerTask { doWork() }
        timer.scheduleAtFixedRate(task, 0L, 10_000L)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doWork() {
        context?.let {
            list.clear()
            list.addAll(EmailFetcher(it).fetchEmails(user.email.split("@")[0]))

            callDelayed(1L) {
                showLabel()
                messagesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showLabel() {
        if (list.isEmpty()) {
            binding.rvMessages.visibility = View.GONE
            binding.tvNoEmails.visibility = View.VISIBLE
        } else {
            binding.rvMessages.visibility = View.VISIBLE
            binding.tvNoEmails.visibility = View.GONE
        }
    }

    private fun setupEvents() {
        binding.root.setOnRefreshListener {
            refresh()
        }

        binding.tvEmail.setOnLongClickListener {
            val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText("label", user.email)
            clipboard.setPrimaryClip(data)

            Toast.makeText(requireContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()

            true
        }

        binding.llLocation.setOnClickListener {
            ViewLocationBottomSheet(requireContext(), user).show()
        }

        binding.ivSave.setOnClickListener {
            bookmarkUser()
        }
    }

    private fun setupUI() {
        binding.tvFullname.text = user.fullName
        binding.tvEmail.text = user.email
        binding.ivPhoto.setImageDrawable(user.getPicture(requireContext()))

        user.getCountryFlag(requireContext())?.let {
            binding.ivFlag.setImageDrawable(it.getImage(requireContext()))
        }

        binding.tvCountry.text = user.country

        messagesAdapter = MessagesListAdapter(list, requireContext())
        binding.rvMessages.apply {
            adapter = messagesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        if (userBookmarked) {
            binding.ivSave.setImageResource(R.drawable.bookmark_added)
        } else {
            binding.ivSave.setImageResource(R.drawable.bookmark_add)
        }

        showLabel()
    }

    fun setUser(it: User) {
        intent.putExtra(INTENT_USER, it)
    }
}