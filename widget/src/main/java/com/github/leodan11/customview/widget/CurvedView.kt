package com.github.leodan11.customview.widget

class CurvedView(
    val startColor: Int,
    val endColor: Int,
    val curvature: Curvature) {

    data class Builder(
        var startColor: Int = 0,
        var endColor: Int = 0,
        var curvature: Curvature = Curvature.MEDIUM) {

        fun startColor(startColor: Int) = apply { this.startColor = startColor }
        fun endColor(endColor: Int) = apply { this.endColor = endColor }
        fun curvature(curvature: Curvature) = apply { this.curvature = curvature }

        fun build() = CurvedView(startColor, endColor, curvature)
    }

    enum class Curvature {
        HIGH, MEDIUM, LOW
    }

}
