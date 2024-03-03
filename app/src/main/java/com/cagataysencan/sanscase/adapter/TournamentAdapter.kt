package com.cagataysencan.sanscase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.databinding.TournamentCardViewBinding
import com.cagataysencan.sanscase.model.Match

class TournamentAdapter(private val matches: Map<String?, List<Match?>>) : RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>() {
    inner class TournamentViewHolder(var view: TournamentCardViewBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<TournamentCardViewBinding>(inflater, R.layout.tournament_card_view, parent, false)
        return TournamentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        holder.view.match = matches.values.toList()[position][0]
        val linearLayoutManager = LinearLayoutManager(holder.view.matchRecyclerView.context, RecyclerView.VERTICAL,false)
        val matchAdapter = MatchAdapter(matches.values.toList()[position])
        val dividerItemDecoration = DividerItemDecoration(holder.view.matchRecyclerView.context, LinearLayoutManager.VERTICAL)
        holder.view.matchRecyclerView.addItemDecoration(dividerItemDecoration)
        holder.view.matchRecyclerView.layoutManager = linearLayoutManager
        holder.view.matchRecyclerView.adapter = matchAdapter
    }


}