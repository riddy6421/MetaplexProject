package com.example.metaplexproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NFT(
    val image: String?,
    val name: String?,
    @PrimaryKey
    val mintKey: String
    )
