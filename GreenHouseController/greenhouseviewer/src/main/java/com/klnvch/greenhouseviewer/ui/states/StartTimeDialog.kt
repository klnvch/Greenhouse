package com.klnvch.greenhouseviewer.ui.states

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.klnvch.greenhousecommon.db.AppSettings
import com.klnvch.greenhousecommon.di.Injectable
import com.klnvch.greenhousecommon.ui.settings.OnDeviceIdDialogActionListener
import com.klnvch.greenhouseviewer.databinding.FragmentStartTimeBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class StartTimeDialog : DialogFragment(), Injectable, OnDeviceIdDialogActionListener {

    private lateinit var binding: FragmentStartTimeBinding

    @Inject
    protected lateinit var settings: AppSettings

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartTimeBinding.inflate(layoutInflater, container, true)
        binding.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val startTime = settings.getStartTime()
        if (startTime > 0) {
            binding.value.setText(sdf.format(Date(startTime)))
        } else {
            binding.value.text = null
        }
    }

    override fun onOkButtonClicked() {
        val startTime = binding.value.text.toString()
        try {
            val date = sdf.parse(startTime)
            if (date != null) {
                settings.setStartTime(date.time)
                dismiss()
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun onCancelButtonClicked() {
        dismiss()
    }
}