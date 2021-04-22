package com.klnvch.greenhousecommon.ui.action

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klnvch.greenhousecommon.R
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
        val item = dataSet[position]
        holder.binding.item = item
        when (item.state) {
            Action.SENT -> holder.binding.state.setImageResource(R.drawable.ic_action_sent)
            Action.SUCCESS -> holder.binding.state.setImageResource(R.drawable.ic_action_success)
            Action.FAIL -> holder.binding.state.setImageResource(R.drawable.ic_action_fail)
            else -> holder.binding.state.setImageBitmap(null)
        }
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
