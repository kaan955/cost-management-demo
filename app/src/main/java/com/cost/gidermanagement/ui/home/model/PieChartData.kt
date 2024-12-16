package com.demo.gidermanagement.ui.home.model

data class PieChartData(
    var browserName: String?,
    var value: Float?
)

// on below line we are creating a method
// in which we are passing all the data.
@Suppress("MagicNumber")
val getPieChartData = listOf(
    PieChartData("Chrome", 80.00F),
    PieChartData("Firefox", 20.00F),
)
