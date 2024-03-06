package com.cagataysencan.sanscase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.constant.Constants
import com.cagataysencan.sanscase.databinding.TournamentCardViewBinding
import com.cagataysencan.sanscase.model.Match

class TournamentAdapter(private var matchHashMap: HashMap<String, List<Match>>?, private val onItemClickListener: MatchAdapter.OnItemClickListener) : RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder>() {
    inner class TournamentViewHolder(var view: TournamentCardViewBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<TournamentCardViewBinding>(inflater, R.layout.tournament_card_view, parent, false)
        return TournamentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matchHashMap?.size ?: 0
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        matchHashMap?.values?.let { matchesList ->
            holder.view.tournamentName = matchHashMap!!.keys.toList()[position]
            holder.view.tournamentFlag = if (matchHashMap!!.keys.toList()[position] == Constants.FAVORITES) "Default Image" else matchesList.toList()[position].first().tournament!!.flag
            val linearLayoutManager = LinearLayoutManager(holder.view.matchRecyclerView.context, RecyclerView.VERTICAL,false)
            val matchAdapter = MatchAdapter(matchesList.toList()[position], onItemClickListener)
            val dividerItemDecoration = DividerItemDecoration(holder.view.matchRecyclerView.context, LinearLayoutManager.VERTICAL)
            holder.view.matchRecyclerView.addItemDecoration(dividerItemDecoration)
            holder.view.matchRecyclerView.layoutManager = linearLayoutManager
            holder.view.matchRecyclerView.adapter = matchAdapter
        }
    }

    fun updateMatches(matches: HashMap<String, List<Match>>) {
        this.matchHashMap = matches
        this.notifyDataSetChanged()
    }

}