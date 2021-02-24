package com.klnvch.greenhousecommon.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.klnvch.greenhousecommon.databinding.FragmentDeviceIdBinding
import com.klnvch.greenhousecommon.db.AppSettings
import com.klnvch.greenhousecommon.di.Injectable
import javax.inject.Inject

open class DeviceIdDialog : DialogFragment(), Injectable, OnDeviceIdDialogActionListener {

    private lateinit var binding: FragmentDeviceIdBinding

    @Inject
    protected lateinit var settings: AppSettings

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeviceIdBinding.inflate(layoutInflater, container, true)
        binding.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deviceId = settings.getDeviceId()
        binding.deviceIdInput.setText(deviceId)
    }

    override fun onOkButtonClicked() {
        val deviceId = binding.deviceIdInput.text.toString()
        settings.setDeviceId(deviceId)
        dismiss()
    }

    override fun onCancelButtonClicked() {
        dismiss()
    }
}
