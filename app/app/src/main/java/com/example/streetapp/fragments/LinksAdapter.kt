package com.example.streetapp.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.databinding.LinksItemBinding
import com.example.streetapp.models.Link

class LinksAdapter(val links: ArrayList<Link>) : RecyclerView.Adapter<LinksAdapter.LinksHolder>() {






    inner class LinksHolder(val binding: LinksItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(link: Link) {
            binding.linkTitle.text = link.title
            binding.urlAddress.text = link.url
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LinksItemBinding>(inflater, R.layout.links_item, parent, false)
        return LinksHolder(binding)
    }

    override fun getItemCount(): Int {
        return links.size
    }

    override fun onBindViewHolder(holder: LinksHolder, position: Int) {
        val link = links[position]
        holder.bind(link)
    }

    fun addLink(link: Link){
        links.add(link)
    }

}