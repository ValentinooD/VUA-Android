package valentinood.vuamobileapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import valentinood.vuamobileapp.databinding.ActivityErrorBinding
import valentinood.vuamobileapp.model.EnumError
import kotlin.system.exitProcess

const val INTENT_ERROR_TYPE = "valentinood.vuamobileapp.ErrorActivity.ERROR_TYPE"
const val INTENT_ERROR_THROWN = "valentinood.vuamobileapp.ErrorActivity.THROWABLE"

class ErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        window.statusBarColor = getColor(R.color.error)

        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(INTENT_ERROR_TYPE)) {

            val error = intent.getStringExtra(INTENT_ERROR_TYPE)?.let { EnumError.valueOf(it) }

            error?.let {
                binding.tvErrorTitle.text = getString(it.resTitle)
                binding.tvErrorDescription.text = getString(it.resDescription)
            }
        }

        if (intent.hasExtra(INTENT_ERROR_THROWN)) {
            val throwable: Throwable?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                throwable = intent.getSerializableExtra(INTENT_ERROR_THROWN, Exception::class.java)
            } else {
                @Suppress("deprecation")
                throwable = intent.getSerializableExtra(INTENT_ERROR_THROWN) as Throwable?
            }

            throwable?.let {
                binding.tvException.text = throwable.stackTraceToString()
            }
        }

        binding.btnRestart.setOnClickListener {
            restart()
        }
    }

    private fun restart() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent)

        exitProcess(0)
    }
}