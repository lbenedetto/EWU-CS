package com.benedetto.lars.ashman.enums

enum class Direction constructor(val value: Int) {
    UP(3), DOWN(1), LEFT(2), RIGHT(0);

    companion object {

        fun getOpposite(direction: Direction): Direction {
            return when (direction) {
                UP -> DOWN
                DOWN -> UP
                LEFT -> RIGHT
                RIGHT -> LEFT
            }
        }

        fun getPerpendicular(direction: Direction): Direction {
            return when (direction) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        }
    }

}