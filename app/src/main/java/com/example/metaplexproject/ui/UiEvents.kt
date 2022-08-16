package com.example.metaplexproject.ui

import androidx.paging.PagingData
import com.example.metaplexproject.model.NFT
import kotlinx.coroutines.flow.Flow

sealed class UiEvents {
    object Idle : UiEvents()
    object Loading : UiEvents()
    object Error : UiEvents()
    data class Data(val data: Flow<PagingData<NFT>>) : UiEvents()
}
