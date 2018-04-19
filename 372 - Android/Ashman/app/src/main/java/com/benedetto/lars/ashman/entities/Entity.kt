package com.benedetto.lars.ashman.entities

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

import com.benedetto.lars.ashman.MazeView
import com.benedetto.lars.ashman.enums.Direction

import com.benedetto.lars.ashman.enums.Direction.RIGHT

abstract class Entity internal constructor(internal val mazeView: MazeView) {
    internal var directionActual: Direction
    internal var x: Float = 0.toFloat()
    internal var y: Float = 0.toFloat()
    var xIndex: Int = 0
    var yIndex: Int = 0
    private var velocity: Float = 0.toFloat()
    var isInvincible: Boolean = false
    internal var tileSize: Float = 0.toFloat()
    private var height: Int = 0
    private var width: Int = 0

    init {
        directionActual = RIGHT
    }

    abstract fun draw(canvas: Canvas, paint: Paint, transformer: Matrix)

    protected abstract fun getWidth(): Int

    protected abstract fun hitWall()

    protected abstract fun modify()

    fun setDirection(direction: Direction) {
        directionActual = direction
    }

    fun setVelocity(velocity: Float) {
        this.velocity = velocity
    }

    fun move() {
        when (directionActual) {
            Direction.UP -> {
                y -= velocity
                if (y < 0) {
                    y = 0f
                    hitWall()
                } else if (isInWall(true, false, 0, 0)) {
                    y += velocity
                    hitWall()
                }
            }
            Direction.DOWN -> {
                y += velocity
                if (y + tileSize > height || isInWall(true, true, 0, 0)) {
                    y -= velocity
                    hitWall()
                }
            }
            Direction.LEFT -> {
                x -= velocity
                if (x < 0) {
                    x = 0f
                    hitWall()
                } else if (isInWall(true, false, 0, 0)) {
                    x += velocity
                    hitWall()
                }
            }
            RIGHT -> {
                x += velocity
                if (x + tileSize > width * 14 || isInWall(true, true, 0, 0)) {
                    x -= velocity
                    hitWall()
                }
            }
        }
        xIndex = (x / tileSize).toInt()
        yIndex = (y / tileSize).toInt()
    }

    internal fun getWallType(Y: Int, X: Int): Int {
        return try {
            Integer.valueOf(mazeView.maze[Y][X].split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
        } catch (e: ArrayIndexOutOfBoundsException) {
            WALL
        }

    }

    internal fun isWall(wallType: Int): Boolean {
        return when (wallType) {
            BLANK, CAKE -> false
            else -> return true
        }
    }

    internal fun isInWall(modify: Boolean, bottomRight: Boolean, xOffset: Int, yOffset: Int): Boolean {
        val buffer = getWidth()
        if (bottomRight) {
            xIndex = ((x + buffer) / tileSize).toInt()
            yIndex = ((y + buffer) / tileSize).toInt()
        } else {
            xIndex = (x / tileSize).toInt()
            yIndex = (y / tileSize).toInt()
        }
        val wallType = getWallType(yIndex + yOffset, xIndex + xOffset)
        if (wallType == CAKE && modify) modify()
        return isWall(wallType)
    }

    open fun setTileSize(tileSize: Float) {
        this.tileSize = tileSize
    }

    fun setWidth(width: Int) {
        this.width = width
    }

    fun setHeight(height: Int) {
        this.height = height
    }

    companion object {
        private const val BLANK = 0
        private const val WALL = 5
        private const val CAKE = 6
    }
}
