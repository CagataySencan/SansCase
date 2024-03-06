package com.cagataysencan.sanscase.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.adapter.MatchAdapter
import com.cagataysencan.sanscase.adapter.TournamentAdapter
import com.cagataysencan.sanscase.databinding.FragmentMainBinding
import com.cagataysencan.sanscase.model.Match
import com.cagataysencan.sanscase.service.NetworkResult
import com.cagataysencan.sanscase.util.createAlertDialogWithAction
import com.cagataysencan.sanscase.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), MatchAdapter.OnItemClickListener {
    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentMainBinding
    private lateinit var tournamentAdapter: TournamentAdapter
    private lateinit var favoriteMatchAdapter: MatchAdapter
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
        tournamentAdapter = TournamentAdapter(HashMap<String, List<Match>>(), this)
        binding.tournamentRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.tournamentRecyclerView.adapter = tournamentAdapter

        favoriteMatchAdapter = MatchAdapter(ArrayList<Match>(), this)
        binding.favoriteMatchRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.favoriteMatchRecyclerView.adapter = favoriteMatchAdapter

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.getMatches()
        viewModel.getFavoriteMatches()

        viewModel.matchesResponse.observe(viewLifecycleOwner, Observer { matchesResponse ->
            when (matchesResponse) {
                is NetworkResult.Success -> {
                    tournamentAdapter.updateMatches(matchesResponse.matchesMap)
                }
                is NetworkResult.Error -> {
                    createAlertDialogWithAction(this@MainFragment.requireContext(), getString(R.string.no_matches_found), getString(R.string.retry) , viewModel::getMatches)
                }
            }
        })

        viewModel.favoriteMatches.observe(viewLifecycleOwner, Observer { favoriteMatches ->
            favoriteMatchAdapter.updateMatches(favoriteMatches)
            if(favoriteMatchAdapter.itemCount == 0) {
                binding.favoriteMatchesLayout.visibility = View.GONE
            } else {
                binding.favoriteMatchesLayout.visibility = View.VISIBLE
            }
        })
    }

    override fun onItemClick(match: Match) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(match)
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onFavoriteClick(match: Match, view: ImageView) {
        viewModel.toggleFavorite(match)
    }
}