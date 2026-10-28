package com.example.a5_2_melekhova

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private var items: List<Movie>,
    private val onEdit: (Movie) -> Unit,
    private val onDelete: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val m = items[position]
        holder.tvTitle.text = m.title
        holder.tvInfo.text = "Год: ${m.year}\nЖанр: ${m.genre}"
        holder.tvRating.text = "★ ${m.rating}"
        loadPoster(holder.itemView.context, m.poster, holder.ivPoster)
        holder.btnEdit.setOnClickListener { onEdit(m) }
        holder.btnDelete.setOnClickListener { onDelete(m) }
    }

    override fun getItemCount(): Int = items.size

    fun setData(newItems: List<Movie>) {
        items = newItems
        notifyDataSetChanged()
    }
}