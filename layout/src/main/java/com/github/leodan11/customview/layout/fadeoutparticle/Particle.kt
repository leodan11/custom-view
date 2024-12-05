package com.github.leodan11.customview.layout.fadeoutparticle

internal class Particle(
    val cx: Float,
    val cy: Float,
    val radius: Float,
    val color: Int,
    val pathType: Int,
) {
    override fun toString(): String = "[$cx:$cy] ($radius) #$color"
}
