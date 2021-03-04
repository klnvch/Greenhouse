package com.klnvch.greenhousecommon.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.klnvch.greenhousecommon.databinding.FragmentActionBinding
import com.klnvch.greenhousecommon.di.Injectable
import com.klnvch.greenhousecommon.di.ViewModelFactory
import javax.inject.Inject

class ActionFragment : Fragment(), Injectable, OnActionButtonListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: FragmentActionBinding
    lateinit var viewModel: ActionViewModel
    private lateinit var adapter: ActionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActionBinding.inflate(inflater, container, true)
        binding.listener = this

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        adapter = ActionAdapter()
        binding.list.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
                .get(ActionViewModel::class.java)
        viewModel.getViewState().observe(viewLifecycleOwner, this::onStateChanged)
    }

    private fun onStateChanged(state: ActionViewState) {
        adapter.update(state.actions)
    }

    override fun onSendButtonClicked() {
        viewModel.sendCommand(binding.textCommand.text.toString())
    }

    override fun onHelpButtonClicked() {

    }
}
