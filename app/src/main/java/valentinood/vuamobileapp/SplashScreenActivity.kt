package valentinood.vuamobileapp

import android.animation.AnimatorSet
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import org.osmdroid.config.Configuration
import valentinood.vuamobileapp.api.user.RandomApiWorker
import valentinood.vuamobileapp.api.user.WORK_FETCH_PROFILE
import valentinood.vuamobileapp.databinding.ActivitySplashScreenBinding
import valentinood.vuamobileapp.framework.applyAnimators
import valentinood.vuamobileapp.framework.getApplicationVersion
import valentinood.vuamobileapp.framework.handleError
import valentinood.vuamobileapp.framework.isOnline
import valentinood.vuamobileapp.model.EnumError

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private var animatorSet: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        // disabling dark mode, evilest thing ever
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.pure_white)

        // this bypasses the new android 12 animation
        splashScreen.setKeepOnScreenCondition { false }

        startAnimations()
        redirect()
    }

    private fun redirect() {
        Configuration.getInstance().userAgentValue = "Mozilla/5.0 Android " + Build.VERSION.SDK_INT + " " + getString(R.string.app_name) + " " + getApplicationVersion()

        if (isOnline()) {
            WorkManager.getInstance(this).apply {
                enqueueUniqueWork(
                    WORK_FETCH_PROFILE,
                    ExistingWorkPolicy.KEEP,
                    OneTimeWorkRequest.Companion.from(RandomApiWorker::class.java)
                )
            }
        } else {
            handleError(EnumError.NOT_ONLINE)
        }
    }

    private fun startAnimations() {
        animatorSet = binding.ivSplashIcon.applyAnimators(R.animator.splash_icon)
    }
}

