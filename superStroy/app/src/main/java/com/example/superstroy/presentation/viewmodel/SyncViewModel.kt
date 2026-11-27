package com.example.superstroy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.superstroy.data.worker.SyncDefectsWorker
import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.usecase.GetDefectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val getDefectsUseCase: GetDefectsUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    // Реальные дефекты из Room
    val defects: StateFlow<List<Defect>> = getDefectsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    sealed class SyncState {
        object Idle : SyncState()
        object Syncing : SyncState()
        data class Error(val message: String) : SyncState()
    }

    fun startSync() {
        _syncState.value = SyncState.Syncing

        val syncRequest = OneTimeWorkRequestBuilder<SyncDefectsWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        workManager.enqueue(syncRequest)

        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(syncRequest.id).collect { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> _syncState.value = SyncState.Idle
                    WorkInfo.State.FAILED -> _syncState.value = SyncState.Error("Ошибка сети")
                    WorkInfo.State.RUNNING -> _syncState.value = SyncState.Syncing
                    else -> {}
                }
            }
        }
    }
}
