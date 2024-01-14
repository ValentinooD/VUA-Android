package valentinood.vuamobileapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import valentinood.vuamobileapp.R
import valentinood.vuamobileapp.databinding.FragmentAboutBinding
import valentinood.vuamobileapp.framework.getApplicationVersion
import valentinood.vuamobileapp.framework.handleError
import valentinood.vuamobileapp.model.EnumError
import java.lang.RuntimeException
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timerTask

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private lateinit var task: TimerTask

    private var timer = Timer()
    private var counter: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        binding.tvVersion.text = getString(R.string.version, context?.getApplicationVersion())

        binding.ivAppIcon.setOnClickListener {
            counter++

            // so only the first time it's clicked
            if (counter == 1) {
                task = timerTask {
                    counter = 0
                }
                timer.schedule(task, 5_000L)
            }

            if (counter == 5) {
                context?.handleError(EnumError.EASTER_EGG, RuntimeException("Found it"))
            }
        }

        return binding.root
    }


}