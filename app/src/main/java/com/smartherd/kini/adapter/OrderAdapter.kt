package com.smartherd.kini.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartherd.kini.R
import com.smartherd.kini.data.Order
import com.smartherd.kini.databinding.OrderItemBinding
import java.text.SimpleDateFormat
import java.util.*


class OrderAdapter : ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    class OrderViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
//            binding.tvOrderId.text = "Order ID: ${order.id}"
            binding.tvOrderId.text = "Order ID: ${order.id}"
            binding.tvOrderDate.text = order.date

            // Bind Order Date
            val date = Date(order.timestamp)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.tvOrderDate.text = "Date: ${dateFormat.format(date)}"

            // Set color based on order status
            val statusColor = if (order.status.lowercase() == "completed") R.color.green else R.color.red
            binding.imageOrderState.setColorFilter(
                ContextCompat.getColor(binding.root.context, statusColor)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}
//class OrderAdapter : ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {
//
//    class OrderViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(order: Order) {
//            // Bind Order ID
//            binding.tvOrderId.text = "Order ID: ${order.id}"
//
//            // Bind Order Date
//            val date = Date(order.timestamp)
//            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
//            binding.tvOrderDate.text = "Date: ${dateFormat.format(date)}"
//
//            // Set the state color indicator dynamically
//            binding.imageOrderState.setColorFilter(
//                ContextCompat.getColor(
//                    binding.root.context,
//                    if (order.totalPrice > 0) R.color.green else R.color.red
//                )
//            )
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
//        val binding = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return OrderViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
//        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
//            return oldItem == newItem
//        }
//    }
//}
