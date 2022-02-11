package com.simplemobiletools.smsmessenger.helpers

import android.content.Context
import android.util.Log
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.smsmessenger.R
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SlackNotifier {
    companion object {
        fun notifySlack(context: Context, message: String) {
            ensureBackgroundThread {
                val webhookUrl = URL(context.getString(R.string.slack_url))
                val connection = webhookUrl.openConnection() as HttpsURLConnection
                try {
                    connection.requestMethod = "POST"
                    connection.doOutput = true

                    val writer = OutputStreamWriter(connection.outputStream)
                    writer.write("{\"text\": \"$message\"}")
                    writer.flush()
                    writer.close()
                }
                catch (e: Exception) {
                    Log.println(Log.ERROR, "SlackNotifier", e.localizedMessage)
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
