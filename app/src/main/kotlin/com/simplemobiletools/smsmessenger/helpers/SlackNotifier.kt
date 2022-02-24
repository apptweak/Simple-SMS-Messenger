package com.simplemobiletools.smsmessenger.helpers

import android.content.Context
import android.os.Build
import android.util.JsonWriter
import android.util.Log
import androidx.annotation.RequiresApi
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.smsmessenger.R
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SlackNotifier {
    companion object {
        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        fun notifySlack(context: Context, message: String) {
            ensureBackgroundThread {
                val webhookUrl = URL(context.getString(R.string.slack_url))
                val connection = webhookUrl.openConnection() as HttpsURLConnection
                try {
                    connection.requestMethod = "POST"
                    connection.doOutput = true

                    val writer = OutputStreamWriter(connection.outputStream)

                    val jsonWriter = JsonWriter(writer)
                    jsonWriter.beginObject()
                    jsonWriter.name("text").value(message)
                    jsonWriter.endObject()
                    jsonWriter.close()
                }
                catch (e: Exception) {
                    Log.println(Log.ERROR, "SlackNotifier", e.localizedMessage)
                    throw e
                }
                finally {
                    Log.println(Log.DEBUG, "SlackNotifier", connection.responseCode.toString())
                    Log.println(Log.DEBUG, "SlackNotifier", connection.responseMessage)
                    connection.disconnect()
                }
            }
        }
    }
}
