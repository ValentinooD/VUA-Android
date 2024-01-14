package valentinood.vuamobileapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import valentinood.vuamobileapp.framework.startActivity

class RandomUserReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<MainActivity>(intent)
    }
}