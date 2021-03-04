package com.klnvch.greenhousecommon.ui.action

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klnvch.greenhousecommon.databinding.ItemActionBinding
import com.klnvch.greenhousecommon.models.Action

class ActionAdapter : RecyclerView.Adapter<ActionAdapter.ViewHolder>() {
    private val dataSet: MutableList<Action> = ArrayList()

    class ViewHolder(val binding: ItemActionBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemActionBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = dataSet[position]
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun update(newDataSet: List<Action>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }
}
