package valentinood.vuamobileapp.api.user

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

const val WORK_FETCH_PROFILE = "valentinood.vuamobileapp.worker.FETCH_PROFILE"
class RandomApiWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        RandomApiFetcher(context).fetch()
        return Result.success()
    }
}