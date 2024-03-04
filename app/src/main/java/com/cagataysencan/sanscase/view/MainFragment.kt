package com.cagataysencan.sanscase.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.adapter.TournamentAdapter
import com.cagataysencan.sanscase.databinding.FragmentMainBinding
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.service.NetworkResult
import com.cagataysencan.sanscase.viewmodel.MainViewModel


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var tournamentAdapter: TournamentAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateFragment()
    }

    private fun initiateFragment() {
        tournamentAdapter =  TournamentAdapter(HashMap<String, List<Match>>())
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.tournamentRecyclerView.layoutManager = linearLayoutManager
        binding.tournamentRecyclerView.adapter = tournamentAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getMatches()

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.matchesResponse.observe(viewLifecycleOwner, Observer { matchesResponse ->
            when (matchesResponse) {
                is NetworkResult.Success -> {
                    matchesResponse.data?.let { matchesMap ->
                        tournamentAdapter.updateMatches(matchesMap)
                    }
                }
                is NetworkResult.Error -> {
                    matchesResponse.exception.let { exception ->

                    }
                }
            }
        })
    }
}