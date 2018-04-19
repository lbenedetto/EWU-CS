package com.benedetto.lars.ashman.enums

import java.util.Random

enum class GhostColor {
    RED, ORANGE, PINK, BLUE;

    companion object {

        val randomColor: GhostColor
            get() {
                val random = Random()
                val rand = random.nextInt(4)
                return when (rand) {
                    0 -> RED
                    1 -> ORANGE
                    2 -> PINK
                    else -> BLUE
                }
            }
    }
}
