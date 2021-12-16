package com.kernel.scanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kernel.scanner.databinding.CargoItemBinding
import com.kernel.scanner.databinding.SealItemBinding
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.model.Seal

class SealListAdapter(val onClickAction:SealClickListener): RecyclerView.Adapter<SealListAdapter.ViewHolder>() {
    private var listSeal= listOf<Seal>()
    fun setupData(list:List<Seal>){
        listSeal=list
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: SealItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(seal: Seal){
            binding.sealText.text=seal.number
            binding.root.setOnClickListener {
                onClickAction.onItemClick(seal)
            }
            binding.buttonDelete.setOnClickListener {
                onClickAction.onDeleteClick(seal)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= SealItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSeal[position])
    }

    override fun getItemCount()=listSeal.size
    interface SealClickListener{
        fun onItemClick(seal: Seal)
        fun onDeleteClick(seal: Seal)
    }
}
