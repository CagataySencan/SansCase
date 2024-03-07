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
import androidx.recyclerview.widget.DividerItemDecoration
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
        tournamentAdapter = TournamentAdapter(ArrayList(),this)
        binding.tournamentRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.tournamentRecyclerView.adapter = tournamentAdapter

        favoriteMatchAdapter = MatchAdapter(ArrayList<Match>(), this)
        binding.favoriteMatchRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.favoriteMatchRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.favoriteMatchRecyclerView.adapter = favoriteMatchAdapter

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getMatches()
            binding.swipeLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.getMatches()

        // Observes the response of the matches.
        viewModel.matchesResponse.observe(viewLifecycleOwner, Observer { matchesResponse ->
            when (matchesResponse) {
                is NetworkResult.Success -> {
                    tournamentAdapter.updateMatches(matchesResponse.matchList)
                }
                is NetworkResult.Error -> {
                    createAlertDialogWithAction(this@MainFragment.requireContext(), getString(R.string.no_matches_found), getString(R.string.retry) , viewModel::getMatches)
                }
            }
        })

        // Observes the favorite matches from database.
        viewModel.favoriteMatches.observe(viewLifecycleOwner, Observer { favoriteMatches ->
            favoriteMatchAdapter.updateMatches(favoriteMatches)
            if(favoriteMatchAdapter.itemCount == 0) {
                binding.favoriteMatchesLayout.visibility = View.GONE
            } else {
                binding.favoriteMatchesLayout.visibility = View.VISIBLE
            }
        })

        // Observes the loading state, controls the visibility of the progress bar and swipe refresh layout
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            when (loading) {
                true -> {
                    binding.swipeLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                false -> {
                    binding.swipeLayout.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE

                }
            }
        })
    }

    // Handles the click event of row. Navigates to DetailFragment with the selected match.
    override fun onItemClick(match: Match) {
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(match)
        Navigation.findNavController(requireView()).navigate(action)
    }

    // Handles the click event of favorite button.
    override fun onFavoriteClick(match: Match, view: ImageView) {
        viewModel.toggleFavorite(match)
    }
}