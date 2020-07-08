package com.example.streetapp.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.databinding.LinksItemBinding

class LinksAdapter(val links: ArrayList<String>) : RecyclerView.Adapter<LinksAdapter.LinksHolder>() {






    inner class LinksHolder(val binding: LinksItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(link: String) {
            binding.linkTitle.text = link
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LinksItemBinding.inflate(inflater)
        return LinksHolder(binding)
    }

    override fun getItemCount(): Int {
        return links.size
    }

    override fun onBindViewHolder(holder: LinksHolder, position: Int) {
        val link = links[position]
        holder.bind(link)
    }

    fun addLink(link: String){
        links.add(link)
    }

}