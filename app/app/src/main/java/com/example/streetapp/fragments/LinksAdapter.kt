package com.example.streetapp.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.databinding.LinksItemBinding
import com.example.streetapp.models.Link

class LinksAdapter(var links: ArrayList<Link>?, val onClearClickListener: OnClearClickListener) : RecyclerView.Adapter<LinksAdapter.LinksHolder>() {


    interface OnClearClickListener {
        fun onClick(link: Link)
        fun onClickLink(link: Link)
    }




    inner class LinksHolder(val binding: LinksItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(link: Link) {
            binding.linkTitle.text = link.title
            binding.urlAddress.text = link.url

            binding.clearImageView.setOnClickListener {
                onClearClickListener.onClick(link)
            }

            binding.root.setOnClickListener {
                Log.i("LinksAdapter", "on click link listener")
                onClearClickListener.onClickLink(link)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<LinksItemBinding>(inflater, R.layout.links_item, parent, false)
        return LinksHolder(binding)
    }

    override fun getItemCount(): Int {
        return links?.size ?: 0
    }

    override fun onBindViewHolder(holder: LinksHolder, position: Int) {
        val link = links?.get(position)
        if (link != null) {
            holder.bind(link)
        }
    }

    fun addLink(link: Link){
        links?.add(link)
    }

}