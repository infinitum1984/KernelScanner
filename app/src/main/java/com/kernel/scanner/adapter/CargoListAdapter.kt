package com.kernel.scanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kernel.scanner.databinding.CargoItemBinding
import com.kernel.scanner.model.Cargo

class CargoListAdapter(val onClickAction:(Cargo)->Unit): RecyclerView.Adapter<CargoListAdapter.ViewHolder>() {
    private var listCargo= listOf<Cargo>()
    fun setupData(list:List<Cargo>){
        listCargo=list
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:CargoItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(cargo: Cargo){
            binding.textViewCarNum.text=cargo.carNumber
            binding.textViewTrailerNum.text=cargo.trailerNumber
            if (cargo.sealNumber.isNotEmpty())
            {
                binding.textViewSeal.text=cargo.sealNumber

            }
            binding.root.setOnClickListener {
                onClickAction.invoke(cargo)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=CargoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCargo[position])
    }

    override fun getItemCount()=listCargo.size
}