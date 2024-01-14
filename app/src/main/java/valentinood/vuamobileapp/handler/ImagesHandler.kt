package valentinood.vuamobileapp.handler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import valentinood.vuamobileapp.factory.createGetHttpUrlConnection
import valentinood.vuamobileapp.framework.handleError
import valentinood.vuamobileapp.model.EnumError
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths

fun storeImage(context: Context, url: String, format: String = "") : File? {
    val url0 = url.format(format.lowercase())
    val filename = url0.substring(url0.lastIndexOf(File.separatorChar) + 1)
    val file = createFile(context, filename)

    try {
        val con: HttpURLConnection = createGetHttpUrlConnection(url0)
        Files.copy(con.inputStream, Paths.get(file.toURI()))

        return file
    } catch (e: Exception) {
        Log.e("ImagesHandler", e.toString(), e)
    }

    return null
}

fun createFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if(file.exists()) file.delete()
    return file
}
