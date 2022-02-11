package com.simplemobiletools.smsmessenger.helpers

import android.util.Log
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SlackNotifier {
    companion object {
        private final val webhookUrl = URL("https://hooks.slack.com/services/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")

        fun notifySlack(message: String) {
            ensureBackgroundThread {
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
                    Log.println(Log.ERROR, "SlackNotifier", e.localizedMessage);
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
