package com.cagataysencan.sanscase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.databinding.MatchCardViewBinding
import com.cagataysencan.sanscase.model.Match

class MatchAdapter(private val matches: ArrayList<Match>) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {
    inner class MatchViewHolder(private val itemView: MatchCardViewBinding) : RecyclerView.ViewHolder(itemView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<MatchCardViewBinding>(inflater, R.layout.match_card_view, parent, false)
        return MatchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        // TODO
    }


}