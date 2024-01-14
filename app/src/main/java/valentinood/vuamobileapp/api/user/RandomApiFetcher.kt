package valentinood.vuamobileapp.api.user

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import valentinood.vuamobileapp.RandomUserReceiver
import valentinood.vuamobileapp.api.email.EMAIL_API_URL
import valentinood.vuamobileapp.fragment.INTENT_USER
import valentinood.vuamobileapp.framework.getCountryFlag
import valentinood.vuamobileapp.framework.handleError
import valentinood.vuamobileapp.framework.sendBroadcast
import valentinood.vuamobileapp.framework.sha1
import valentinood.vuamobileapp.handler.storeImage
import valentinood.vuamobileapp.model.CountryFlag
import valentinood.vuamobileapp.model.EnumError
import valentinood.vuamobileapp.model.Message
import valentinood.vuamobileapp.model.User
import valentinood.vuamobileapp.provider.FLAG_PROVIDER_CONTENT_URI
import java.io.File


const val API_URL = "https://randomuser.me/api/1.4/"
const val FLAGS_API_URL = "https://flagcdn.com/w80/%s.png"
class RandomApiFetcher(private val context: Context) {
    fun fetch() {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(API_URL)
                .get()
                .build()

            client.newCall(request).execute().use { it ->
                it.body()?.let {
                    val json = JSONObject(it.string())
                    val array = json.getJSONArray("results")
                    parseItem(array.getJSONObject(0))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            context.handleError(EnumError.INTERNAL, e, Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun parseItem(json: JSONObject) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val pictureFile = storeImage(context, json.getJSONObject("picture").getString("large"))
            pictureFile!!.renameTo(File(pictureFile.sha1()!!))

            val fullName = json.getJSONObject("name").let {
                it.getString("first") + " " + it.getString("last")
            }

            val country = json.getJSONObject("location").getString("country")
            val email = json.getJSONObject("login").getString("username") + "@byom.de"

            val coordinates = json.getJSONObject("location").getJSONObject("coordinates")

            val user = User(
                null,
                fullName,
                email,
                country,
                json.getString("nat"),
                pictureFile!!.absolutePath,
                coordinates.getDouble("latitude"),
                coordinates.getDouble("longitude")
            )

            var download = true

            // Download again if the file doesn't exist anymore or if the hash doesn't match
            val flag = context.getCountryFlag(user.nationality)
            if (flag != null) {
                val file = File(flag.filePath)
                download = !file.exists() || !file.sha1().equals(flag.fileHash)

                if (download) {
                    context.contentResolver.delete(
                        FLAG_PROVIDER_CONTENT_URI,
                        "${CountryFlag::nat.name}=?",
                        arrayOf(user.nationality)
                    )
                }
            }

            if (download) {
                val countryFlag = storeImage(context, FLAGS_API_URL, user.nationality)
                context.contentResolver.insert(
                    FLAG_PROVIDER_CONTENT_URI,
                    ContentValues().apply {
                        put(CountryFlag::nat.name, user.nationality)
                        put(CountryFlag::fileHash.name, countryFlag!!.sha1())
                        put(CountryFlag::filePath.name, countryFlag.absolutePath)
                    }
                )
            }

            val intent = Intent()
            intent.putExtra(INTENT_USER, user)

            context.sendBroadcast<RandomUserReceiver>(intent)
        }
    }
}
