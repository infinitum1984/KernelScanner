package com.kernel.scanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kernel.scanner.R
import com.kernel.scanner.databinding.CargoItemBinding
import com.kernel.scanner.model.Cargo
import android.text.format.DateUtils




class CargoListAdapter(val onClickAction:(Cargo)->Unit): RecyclerView.Adapter<CargoListAdapter.ViewHolder>() {
    private var listCargo= listOf<Cargo>()
    fun setupData(list:List<Cargo>){
        listCargo=list
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding:CargoItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(cargo: Cargo){
            binding.textViewCarNum.text=binding.root.context
                .getString(R.string.car_lable)+" "+cargo.carNumber
            
            binding.textViewTrailerNum.text=binding.root.context.getString(R.string.trailer_lable)+
                    " "+cargo.trailerNumber
            if (cargo.isChecked)
            {
                binding.imageView.background=ContextCompat.getDrawable(binding.root.context,R.drawable.background_saved)

            }else{
                binding.imageView.background=ContextCompat.getDrawable(binding.root.context,R.drawable.background_queue)

            }
            if(cargo.lastEdit!=0L){
                val now = System.currentTimeMillis()
                val timeTxt=DateUtils.formatDateTime(binding.root.context,now ,DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE)
                binding.textViewTime.text=timeTxt
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