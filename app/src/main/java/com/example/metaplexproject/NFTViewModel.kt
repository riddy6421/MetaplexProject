package com.example.metaplexproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metaplexproject.data.ApiDataRepo
import com.example.metaplexproject.data.DataState
import com.example.metaplexproject.model.NFT
import com.example.metaplexproject.ui.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NFTViewModel @Inject constructor(
    private val apiDataRepo: ApiDataRepo,
    private val ioCoroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val mutableUiEvents: MutableStateFlow<UiEvents> = MutableStateFlow(UiEvents.Idle)
    private lateinit var nftDataState: DataState

    val uiEvents: StateFlow<UiEvents>
        get() = mutableUiEvents

    fun searchPublicKey(publicKey: String) = viewModelScope.launch(ioCoroutineDispatcher) {
        mutableUiEvents.emit(UiEvents.Loading)
        nftDataState = apiDataRepo.getNftData(publicKey)
        observeDataState()
    }

    private fun observeDataState() = viewModelScope.launch(ioCoroutineDispatcher) {
        when(nftDataState) {
            DataState.Success -> {
                apiDataRepo.saveNftsToDB()
                mutableUiEvents.emit(UiEvents.Data(apiDataRepo.getNextPage()))
            }
            else -> mutableUiEvents.emit(UiEvents.Error)
        }
    }
}