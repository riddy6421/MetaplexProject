package com.example.metaplexproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.metaplexproject.NftListViewHolder
import com.example.metaplexproject.databinding.NftItemBinding
import com.example.metaplexproject.model.NFT
import com.squareup.picasso.Picasso

class NftListAdapter: PagingDataAdapter<NFT, NftListViewHolder>(NftItemDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftListViewHolder {
        return NftListViewHolder(NftItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NftListViewHolder, position: Int) {
        val nft = getItem(position)
        holder.nftBinding.apply {
            holder.itemView.apply {
                Picasso
                    .get()
                    .load(nft?.image)
                    .into(nftImage)
                nftName.text = nft?.name
                nftMint.text = nft?.mintKey
            }
        }
    }

}

class NftItemDiffCallBack: DiffUtil.ItemCallback<NFT>() {
    override fun areItemsTheSame(oldItem: NFT, newItem: NFT): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: NFT, newItem: NFT): Boolean {
        return oldItem == newItem
    }
}