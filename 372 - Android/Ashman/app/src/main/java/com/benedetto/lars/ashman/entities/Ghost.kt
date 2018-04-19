package com.benedetto.lars.ashman.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

import com.benedetto.lars.ashman.MazeView
import com.benedetto.lars.ashman.R
import com.benedetto.lars.ashman.enums.Direction
import com.benedetto.lars.ashman.enums.GhostColor

import java.util.Random

class Ghost(context: Context, ghostColor: GhostColor, mazeView: MazeView) : Entity(mazeView) {
    private var ghost: Bitmap? = null

    init {
        x = 0f
        y = 0f
        ghost = when (ghostColor) {
            GhostColor.BLUE -> BitmapFactory.decodeResource(context.resources, R.drawable.ghost_blue)
            GhostColor.ORANGE -> BitmapFactory.decodeResource(context.resources, R.drawable.ghost_orange)
            GhostColor.PINK -> BitmapFactory.decodeResource(context.resources, R.drawable.ghost_pink)
            GhostColor.RED -> BitmapFactory.decodeResource(context.resources, R.drawable.ghost_red)
        }
        isInvincible = true
    }

    override fun draw(canvas: Canvas, paint: Paint, transformer: Matrix) {
        transformer.reset()
        val scale = tileSize * .8f / ghost!!.width
        transformer.postScale(scale, scale)
        transformer.postTranslate(x, y)
        canvas.drawBitmap(ghost!!, transformer, paint)
    }

    public override fun getWidth(): Int {
        return ghost!!.width
    }

    public override fun hitWall() {
        val random = Random()
        var rand = random.nextInt(5)//rand range 0-4 (inclusive)
        var newDirection = directionActual
        var perpendicularDirection = Direction.getPerpendicular(directionActual)
        while (!isDirectionAllowed(newDirection)) {
            newDirection = when {
                //rand 0 or 1
                rand < 2 -> perpendicularDirection
                //rand 2 or 3
                rand < 4 -> Direction.getOpposite(perpendicularDirection)
                //rand 4. 1 in 5 chance to turn around
                else -> Direction.getOpposite(directionActual)
            }
            perpendicularDirection = Direction.getOpposite(perpendicularDirection)
            rand = random.nextInt(5)
        }
        setDirection(newDirection)
    }

    public override fun modify() {

    }

    private fun isDirectionAllowed(direction: Direction): Boolean {
        return when (direction) {
            Direction.UP -> !isInWall(false, false, 0, -1)
            Direction.DOWN -> !isInWall(false, true, 0, 1)
            Direction.LEFT -> !isInWall(false, false, -1, 0)
            Direction.RIGHT -> !isInWall(false, true, 1, 0)
        }
    }

    override fun setTileSize(tileSize: Float) {
        this.tileSize = tileSize
        val rand = Random()
        xIndex = rand.nextInt(14)
        yIndex = rand.nextInt(14)
        while (yIndex < 6 && xIndex < 6 || isWall(getWallType(yIndex, xIndex))) {
            xIndex = rand.nextInt(14)
            yIndex = rand.nextInt(14)
        }
        y = yIndex * tileSize
        x = xIndex * tileSize
    }
}
