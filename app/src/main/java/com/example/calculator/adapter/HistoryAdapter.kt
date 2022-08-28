package com.example.calculator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.ViewholderHistoryItemBinding
import com.example.calculator.models.HistoryModel

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val list= arrayListOf<HistoryModel>()

    fun addData(list:List<HistoryModel>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
    inner class HistoryViewHolder(val binding: ViewholderHistoryItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(ViewholderHistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
    val binding=holder.binding
   val data=list[position]
        binding.apply {
            historyText.text=data.exp+"="+data.result
        }
    }

    override fun getItemCount()=list.size


}