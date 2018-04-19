package com.benedetto.lars.ashman.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.MediaPlayer

import com.benedetto.lars.ashman.MazeView
import com.benedetto.lars.ashman.R

class Ashman(context: Context, mazeView: MazeView) : Entity(mazeView) {
    private var frame = 0.0
    private val ashmanFrame: Array<Bitmap>
    private val cakeSound: MediaPlayer

    init {
        x = 0f
        y = 0f
        ashmanFrame = arrayOf(
                BitmapFactory.decodeResource(context.resources, R.drawable.ashman_open),
                BitmapFactory.decodeResource(context.resources, R.drawable.ashman_closed))
        cakeSound = MediaPlayer.create(context, R.raw.cake_sound)
        isInvincible = false
    }

    override fun draw(canvas: Canvas, paint: Paint, transformer: Matrix) {
        transformer.reset()
        val ashmanBitmap = ashmanFrame[frame.toInt() % 2]
        transformer.postRotate((90 * directionActual.value).toFloat(), ashmanBitmap.width / 2f, ashmanBitmap.width / 2f)
        val scale = tileSize * .8f / ashmanFrame[0].width
        transformer.postScale(scale, scale)
        transformer.postTranslate(x, y)
        canvas.drawBitmap(ashmanBitmap, transformer, paint)
        frame += .1
    }

    public override fun getWidth(): Int {
        return ashmanFrame[0].width
    }

    private fun playSound() {
        cakeSound.start()
    }

    public override fun hitWall() {
        //Do nothing
    }

    public override fun modify() {
        mazeView.maze[yIndex][xIndex] = "00"
        mazeView.decrementCakes()
        playSound()
    }
}
