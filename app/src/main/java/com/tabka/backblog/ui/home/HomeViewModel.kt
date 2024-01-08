package com.tabka.backblog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.tabka.backblog.R
import com.tabka.backblog.models.HomeImageItem
import com.tabka.backblog.models.UserLog
import com.tabka.backblog.repos.local_storage.LogsLocalRepo
import java.util.UUID

class HomeViewModel : ViewModel() {
/*
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text*/
    private val TAG = "HomeViewModel"

    private val _userLogList = MutableLiveData<List<UserLog>>()
    private val _logsMap = MutableLiveData<Map<String, UserLog>>()

    private val _imageItemList = MutableLiveData<List<HomeImageItem>>()
    val imageItemList: LiveData<List<HomeImageItem>> = _imageItemList

    init {
        loadLogs()
    }

    private fun loadLogs() {
        val firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            LogsLocalRepo.getLogs().observeForever { logs ->
                transformLogsToImageItems(logs)
            }
        } else {
            TODO("Call LogsRemoteRepo.getLogs()")
        }
    }

    fun createLog(logName: String) {
        val id = UUID.randomUUID().toString()
        val priority = _imageItemList.value?.maxByOrNull { it.priority }?.priority ?: 0 + 1
        val log = UserLog(
            id = id,
            name = logName,
            is_visible = true,
            movie_ids = emptyList(),
            watched_ids = emptyList(),
            priority = priority
        )
        LogsLocalRepo.addLog(log)
        loadLogs()
    }

    private fun transformLogsToImageItems(logs: List<UserLog>) {
        _logsMap.value = logs.associateBy { it.id }

        val imageItems = logs.map { log ->
            val imageResId = R.drawable.img_placeholder_log_batman
            HomeImageItem(id=log.id, image=imageResId, log_name = log.name, priority = log.priority)
        }.sortedBy { it.priority }
        _imageItemList.value = imageItems
    }

    fun updatePrioritiesAndSaveLogs(imageItems: List<HomeImageItem>) {
        val updatedLogs = imageItems.mapNotNull { item ->
            val log = _logsMap.value?.get(item.id) ?: return@mapNotNull null
            log.copy(priority = item.priority)
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            // Save locally
            LogsLocalRepo.reorderLogs(updatedLogs)
        } else {
            // Save to Firebase
            TODO("Implement Firebase save logic")
        }

        // Optionally, you can update the LiveData if necessary
        _userLogList.value = updatedLogs
    }

    override fun onCleared() {
        super.onCleared()
    }

}