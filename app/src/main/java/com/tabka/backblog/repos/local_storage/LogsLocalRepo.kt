package com.tabka.backblog.repos.local_storage
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tabka.backblog.models.UserLog
import com.tabka.backblog.utils.JsonUtility

object LogsLocalRepo {
    private val TAG = "Local Storage Logs Repo"
    private lateinit var jsonUtility: JsonUtility

    private val userLogsList = MutableLiveData<List<UserLog>>()

    fun init(context: Context) {
        jsonUtility = JsonUtility(context)
    }

    fun getLogs(): LiveData<List<UserLog>> {
        val logs = jsonUtility.readFromFile()
        userLogsList.value = logs

        Log.d(TAG, logs.toString())
        return userLogsList
    }

    fun addLog(log: UserLog) {
        jsonUtility.appendToFile(log)
    }

    fun reorderLogs(userLogsJson: List<UserLog>) {
        jsonUtility.overwriteJSON(userLogsJson)
    }


}