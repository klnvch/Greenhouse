package com.klnvch.greenhousecommon.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewModel()
        binding = FragmentActionBinding.inflate(inflater, container, true)
        binding.listener = this

        binding.list.layoutManager = LinearLayoutManager(requireContext())
        adapter = ActionAdapter()
        binding.list.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getViewState().observe(viewLifecycleOwner, this::onStateChanged)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(ActionViewModel::class.java)
    }

    private fun onStateChanged(state: ActionViewState) {
        adapter.update(state.actions)
    }

    override fun onSendButtonClicked() {
        viewModel.sendCommand(binding.textCommand.text.toString())
    }

    override fun onHelpButtonClicked() {
        AlertDialog.Builder(requireContext())
            .setMessage(
                "" +
                        "  0                                               - get data\n" +
                        "  1, yy,mm,dd,hh,mm,ss                            - set time\n" +
                        "  2                                               - get global limits\n" +
                        "  3, minVoltage,fanVoltage,waterLevel,fanTimeout  - set global limits\n" +
                        "  4, sectorId,timeoutSeconds                      - start watering\n" +
                        "  5, sectorId                                     - get watering defaults\n" +
                        "  6, sectorId,minNorth,minSouth,timeoutSeconds    - set watering defaults\n" +
                        "  7, timeoutSeconds                               - turn on global fans\n" +
                        "  8, sectorId,angle                               - move window\n" +
                        "  9, sectorId,timeoutSeconds                      - start fan\n" +
                        "  10,sectorId                                     - get climate defaults\n" +
                        "  11,sectorId, windowTemperature, fanTemperature  - set climate defaults\n" +
                        "  12                                              - help"
            )
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
