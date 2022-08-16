package com.example.metaplexproject.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.metaplexproject.model.NFT



@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNft(nfts: List<NFT>)

    @Query("SELECT * FROM nft")
    fun getAllNfts(): PagingSource<Int, NFT>
}