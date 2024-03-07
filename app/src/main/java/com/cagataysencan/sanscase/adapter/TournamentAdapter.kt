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

class TournamentAdapter(private var matchList: List<List<Match>>, private val onItemClickListener: MatchAdapter.OnItemClickListener) : RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>() {

    inner class TournamentViewHolder(var view: TournamentCardViewBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<TournamentCardViewBinding>(inflater, R.layout.tournament_card_view, parent, false)
        return TournamentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        holder.view.match = matchList[position].first()
        holder.view.matchRecyclerView.addItemDecoration(DividerItemDecoration(holder.view.matchRecyclerView.context, LinearLayoutManager.VERTICAL))
        holder.view.matchRecyclerView.layoutManager = LinearLayoutManager(holder.view.matchRecyclerView.context, RecyclerView.VERTICAL,false)
        holder.view.matchRecyclerView.adapter = MatchAdapter(matchList[position], onItemClickListener)
    }

    fun updateMatches(matchList: List<List<Match>>) {
        this.matchList = matchList
        this.notifyDataSetChanged()
    }
}