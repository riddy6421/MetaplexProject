package com.example.metaplexproject.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.metaplexproject.data.db.DataDaoImpl
import com.example.metaplexproject.model.NFT
import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.indenty.ReadOnlyIdentityDriver
import com.metaplex.lib.drivers.storage.OkHttpSharedStorageDriver
import com.metaplex.lib.solana.SolanaConnectionDriver
import com.solana.core.PublicKey
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ApiDataRepo @Inject constructor(private val dataDaoImpl: DataDaoImpl) {
    private lateinit var ownerPublicKey: PublicKey
    private lateinit var solanaConnection: SolanaConnectionDriver
    private lateinit var solanaIdentityDriver: ReadOnlyIdentityDriver
    private lateinit var storageDriver: OkHttpSharedStorageDriver
    private lateinit var metaplex: Metaplex
    private lateinit var  nftList: MutableList<NFT>

    private var limit: Int = 20
    private var maxSize: Int = 100

    private fun initializeApiRepo(publicKey: String) {
        ownerPublicKey = PublicKey(publicKey)
        solanaConnection = SolanaConnectionDriver(RPCEndpoint.mainnetBetaSolana)
        solanaIdentityDriver = ReadOnlyIdentityDriver(ownerPublicKey, solanaConnection.solanaRPC)
        storageDriver = OkHttpSharedStorageDriver()
        metaplex = Metaplex(solanaConnection, solanaIdentityDriver, storageDriver)
        nftList = mutableListOf()
    }

    suspend fun getNftData(publicKey: String) = suspendCoroutine { cont ->
        initializeApiRepo(publicKey)
        metaplex.nft.findAllByOwner(ownerPublicKey) { nftCallback ->
            nftCallback.onSuccess { nfts ->
                val nonNullFilteredList = nfts.filterNotNull()
                nonNullFilteredList.mapIndexed { index, nft ->
                    val nftMint = nft.mint
                    nft.metadata(metaplex) { result ->
                        result.onSuccess { nftMetadata ->
                            nftList.add(NFT(nftMetadata.image, nftMetadata.name, nftMint.toString()))
                            if (index == (nonNullFilteredList.size - 1)) { //resume when all nfts are added
                                cont.resume(DataState.Success)
                            }
                        }.onFailure {
                        }
                    }
                }
            }.onFailure {
                cont.resume(DataState.Error)
            }
        }
    }


    suspend fun saveNftsToDB() {
        val list: List<NFT> = ArrayList<NFT>(nftList)
        dataDaoImpl.insertNft(list)
    }

    fun getNextPage(): Flow<PagingData<NFT>> {
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                maxSize = maxSize,
                enablePlaceholders = true
            )
        ) { dataDaoImpl.getAllNfts() }.flow
    }

}