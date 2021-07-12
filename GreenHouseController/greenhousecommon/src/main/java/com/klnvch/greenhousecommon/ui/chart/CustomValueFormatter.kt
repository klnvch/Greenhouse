package com.klnvch.greenhousecommon.ui.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CustomValueFormatter : IndexAxisValueFormatter() {
    private val axisSdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val millis = TimeUnit.MINUTES.toMillis(value.toLong())
        return axisSdf.format(Date(millis))
    }
}
