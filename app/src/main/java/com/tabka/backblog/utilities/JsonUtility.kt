package com.tabka.backblog.utilities
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type

class JsonUtility : AppCompatActivity() {
    data class UserLog(
        val id: String,
        val name: String,
        val is_visible: Boolean,
        val movie_ids: List<Int>,
        val watched_ids: List<Int>,
        val priority: Int,
    )

    private val fileName = "logs.json"
    fun appendToFile(context: Context, newLog: UserLog) {
        // Read existing content
        val existingLogs = readFromFile(context)

        // Add the new log to the list
        existingLogs.add(newLog)

        // Convert the updated list back to JSON string
        val updatedJson = Gson().toJson(existingLogs)

        // Write the JSON string to the file
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(updatedJson.toByteArray())
        }
    }

    fun readFromFile(context: Context): MutableList<UserLog> {
        val fileContents = try {
            context.openFileInput(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            return mutableListOf()
        }

        return if (fileContents.startsWith("[")) {
            // The content is a JSON array
            val listType: Type = object : TypeToken<MutableList<UserLog>>() {}.type
            Gson().fromJson(fileContents, listType) ?: mutableListOf()
        } else if (fileContents.isNotEmpty()) {
            // The content is a single JSON object
            mutableListOf(Gson().fromJson(fileContents, UserLog::class.java))
        } else {
            // The file is empty
            mutableListOf()
        }
    }

    fun overwriteJSON(context: Context, logs: List<UserLog>) {
        val json = Gson().toJson(logs)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }
}