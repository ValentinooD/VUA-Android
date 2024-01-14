package valentinood.vuamobileapp.api.email

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import valentinood.vuamobileapp.model.Message

const val EMAIL_API_URL = "https://api.byom.de/mails/"
class EmailFetcher(val context: Context) {

    fun fetchEmails(email: String) : List<Message> {
        val list = mutableListOf<Message>()
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$EMAIL_API_URL/$email")
            .get()
            .build()

        client.newCall(request).execute().use { it ->
            it.body()?.let {
                val json = JSONArray(it.string())

                if (json.length() > 0) {
                    for (i in 0 until json.length()) {
                        val obj = json.getJSONObject(i)

                        val from = obj.getString(Message::from.name).replace("&lt;", "<").replace("&gt;", ">")

                        list.add(
                            Message(
                                obj.getString(Message::to.name),
                                from,
                                obj.getString(Message::subject.name),
                                obj.getString(Message::html.name),
                                obj.getString(Message::text.name)
                            )
                        )
                    }
                }
            }
        }
        return list
    }
}