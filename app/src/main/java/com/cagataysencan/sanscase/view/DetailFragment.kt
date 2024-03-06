package com.cagataysencan.sanscase.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.cagataysencan.sanscase.R
import com.cagataysencan.sanscase.databinding.FragmentDetailBinding
import com.cagataysencan.sanscase.viewmodel.DetailViewModel

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateFragment()
    }
    private fun initiateFragment() {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        arguments?.let {
            binding.match = DetailFragmentArgs.fromBundle(it).match
            binding.viewModel = viewModel
        }
    }
}