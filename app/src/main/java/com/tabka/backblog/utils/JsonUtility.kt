package com.tabka.backblog.utils
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tabka.backblog.models.UserLog
import java.io.IOException
import java.lang.reflect.Type

class JsonUtility(context: Context) : AppCompatActivity() {

    private val fileName = "logs.json"
    private val context: Context = context
    fun appendToFile(newLog: UserLog) {

        // Read existing content
        val existingLogs = readFromFile()

        // Add the new log to the list
        existingLogs.add(newLog)

        // Convert the updated list back to JSON string
        val updatedJson = Gson().toJson(existingLogs)

        // Write the JSON string to the file
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(updatedJson.toByteArray())
        }
    }

    fun readFromFile(): MutableList<UserLog> {
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

    fun overwriteJSON(logs: List<UserLog>) {
        val json = Gson().toJson(logs)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }
}