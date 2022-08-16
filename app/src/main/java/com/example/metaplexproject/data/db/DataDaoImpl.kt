package com.example.metaplexproject.data.db

import androidx.paging.PagingSource
import com.example.metaplexproject.model.NFT

class DataDaoImpl(
    private val dataDao: DataDao
): DataDao {

    override suspend fun insertNft(nfts: List<NFT>) {
        dataDao.insertNft(nfts)
    }

    override fun getAllNfts(): PagingSource<Int, NFT> {
        return dataDao.getAllNfts()
    }
}