package valentinood.vuamobileapp.framework

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import valentinood.vuamobileapp.ErrorActivity
import valentinood.vuamobileapp.INTENT_ERROR_THROWN
import valentinood.vuamobileapp.INTENT_ERROR_TYPE
import valentinood.vuamobileapp.model.EnumError
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Formatter

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun Context.getApplicationVersion(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val pi = packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0L))
        return pi.versionName
    } else {
        @Suppress("deprecation")
        val pi = packageManager.getPackageInfo(packageName, 0)
        return pi.versionName
    }
}

fun File.sha1(): String? {
    return try {
        val md = MessageDigest.getInstance("SHA-1")
        val formatter = Formatter()
        val hash = md.digest(readBytes())
        for (b in hash) {
            formatter.format("%02x", b)
        }
        formatter.toString()
    } catch (ex: NoSuchAlgorithmException) {
        ex.printStackTrace()
        "no hash"
    }
}

fun View.applyAnimation(animationId: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, animationId))

fun View.applyAnimators(vararg animationsId: Int): AnimatorSet {
    val animatorSet = AnimatorSet()
    for (id in animationsId) {
        animatorSet.playTogether(AnimatorInflater.loadAnimator(context, id))
    }

    animatorSet.setTarget(this)
    animatorSet.start()
    return animatorSet
}

fun View.removeFromParent() {
    if (parent !is ViewGroup) return
    (parent as ViewGroup).removeView(this)
}

fun Context.handleError(error: EnumError? = null, throwable: Throwable? = null, flags: Int? = null) {
    val intent = Intent(this, ErrorActivity::class.java)
    flags?.let { intent.setFlags(flags) }
    error?.let{ intent.putExtra(INTENT_ERROR_TYPE, it.name) }
    throwable?.let { intent.putExtra(INTENT_ERROR_THROWN, it) }
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(intent)
}

inline fun <reified T : BroadcastReceiver> Context.sendBroadcast(intent: Intent? = null) =
    sendBroadcast(Intent(this, T::class.java).apply {
        intent?.let { putExtras(intent) }
    })

inline fun <reified T : Activity> Context.startActivity(intent: Intent? = null) =
    startActivity(Intent(this, T::class.java)
        .apply {
            intent?.let { putExtras(it) }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })

fun callDelayed(delay: Long, work: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let {network ->
        connectivityManager.getNetworkCapabilities(network)?.let {networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }
    return false
}


fun Context.setBooleanPreference(key: String, value: Boolean = true)
        = PreferenceManager.getDefaultSharedPreferences(this)
    .edit()
    .putBoolean(key, value)
    .apply()

fun Context.getBooleanPreference(key: String)
        = PreferenceManager.getDefaultSharedPreferences(this)
    .getBoolean(key, false)

