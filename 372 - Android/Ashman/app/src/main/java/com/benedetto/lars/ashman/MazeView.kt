package com.benedetto.lars.ashman

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

import com.benedetto.lars.ashman.entities.Ashman
import com.benedetto.lars.ashman.entities.Entity
import com.benedetto.lars.ashman.entities.Ghost

import com.benedetto.lars.ashman.enums.Direction
import com.benedetto.lars.ashman.enums.GhostColor

class MazeView : View {
    //General stuff
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mIsRunning = false
    private lateinit var mHandler: Handler
    private lateinit var mTimer: Runnable
    private var cakeListener: CakeListener? = null
    //Drawing stuff
    private val paint = Paint()
    private var tileSize: Float = 0.toFloat()
    lateinit var maze: Array<Array<String>>
    private lateinit var bitmaps: Array<Bitmap>
    private lateinit var entities: Array<Entity>
    private var cakes = 121
    var level = 1
    private var ghosts: Int = 0
    private var ghostIncrementer: Int = 0
    private lateinit var gameOver: MediaPlayer
    private lateinit var gameWon: MediaPlayer

    private val transformer = Matrix()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {

        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        ghosts = sp.getInt("startGhosts", 2)
        ghostIncrementer = sp.getInt("ghostsPerLevel", 2)
        loadMaze()
        loadEntities()
        //Load bitmaps
        bitmaps = arrayOf(
                BitmapFactory.decodeResource(resources, R.drawable.blank),
                BitmapFactory.decodeResource(resources, R.drawable.wall_cap),
                BitmapFactory.decodeResource(resources, R.drawable.wall_corner),
                BitmapFactory.decodeResource(resources, R.drawable.wall_intersection3),
                BitmapFactory.decodeResource(resources, R.drawable.wall_intersection4),
                BitmapFactory.decodeResource(resources, R.drawable.wall),
                BitmapFactory.decodeResource(resources, R.drawable.cake))
        mHandler = Handler()
        mTimer = object : Runnable {
            override fun run() {
                onTimer()
                if (mIsRunning) mHandler.postDelayed(this, TIMER_MSEC.toLong())
            }
        }
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.isDither = false
        gameOver = MediaPlayer.create(context, R.raw.game_over)
        gameWon = MediaPlayer.create(context, R.raw.game_won)
    }

    private fun loadEntities() {
        var isAshman = true
        entities = Array(size = ghosts + 1, init = {
            if (isAshman) {
                isAshman = !isAshman
                Ashman(context, this)
            } else {
                Ghost(context, GhostColor.randomColor, this)
            }
        })
    }

    private fun loadMaze() {
        val mazeString = context!!.getString(R.string.maze)
        val tempMaze = mazeString.split(" ")
        var i = 0
        maze = Array(size = 14, init = {
            Array(size = 14, init = {
                tempMaze[i++]
            })
        })
    }

    fun cheat() {
        for (y in 0..13) {
            for (x in 0..13) {
                if (y != 1 || x != 2) {
                    if (maze[y][x] == "60") {
                        maze[y][x] = "00"
                        decrementCakes()
                    }
                }
            }
        }
        invalidate()
    }

    fun decrementCakes() {
        cakes--
        if (cakes == 0) {
            gameWon.start()
            Toast.makeText(context, "You WIN", Toast.LENGTH_SHORT).show()
            incrementLevel()
        }
        cakeListener!!.updateCakes(cakes)

    }

    private fun incrementLevel() {
        ghostIncrementer = PreferenceManager.getDefaultSharedPreferences(context).getInt("ghostsPerLevel", 2)
        ghosts += ghostIncrementer
        level++
        loadMaze()
        loadEntities()
        updateEntities()
        cakes = 122
        decrementCakes()
    }

    override fun onDraw(canvas: Canvas) {
        this.setBackgroundColor(Color.RED)
        var tileX: Int
        var tileY = 0
        var xCoord: Float
        var yCoord = 0f
        //Draw maze
        while (tileY < 14) {
            //Draw a new column
            tileX = 0
            xCoord = 0f
            while (tileX < 14) {
                //The second digit is number of 90degree rotations to apply to the sprite
                val temp = maze[tileY][tileX].split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val wall = intArrayOf(Integer.valueOf(temp[1]), Integer.valueOf(temp[2]))
                val bitmap = bitmaps[wall[0]]
                transformer.reset()
                transformer.postRotate(90f * wall[1], bitmap.width / 2f, bitmap.height / 2f)
                val scale = tileSize / bitmap.width
                transformer.postScale(scale, scale)
                transformer.postTranslate(xCoord, yCoord)
                canvas.drawBitmap(bitmap, transformer, paint)
                tileX++
                xCoord += tileSize
            }
            tileY++
            yCoord += tileSize
        }
        //Draw entities
        for (e in entities) {
            e.draw(canvas, paint, transformer)
        }
    }

    fun registerCakeListener(cakeListener: CakeListener) {
        this.cakeListener = cakeListener
    }

    fun setAshmanDirection(direction: Direction) {
        entities[0].setDirection(direction)
    }

    private fun onTimer() {
        for (e in entities) {
            e.move()
        }
        val ashman = entities[0]
        var ghost: Entity
        for (i in 1 until entities.size) {
            ghost = entities[i]
            if (ashman.xIndex == ghost.xIndex && ashman.yIndex == ghost.yIndex && !ashman.isInvincible) {
                Toast.makeText(context, "You LOSE", Toast.LENGTH_SHORT).show()
                gameOver.start()
                loadMaze()
                loadEntities()
                updateEntities()
                cakes = 121
                pause()
            }
        }
        invalidate()
    }

    fun pausePlay() {
        if (mIsRunning)
            pause()
        else
            play()
    }

    private fun pause() {
        mIsRunning = false
        mHandler.removeCallbacks(mTimer)
    }

    private fun play() {
        mIsRunning = true
        mHandler.postDelayed(mTimer, TIMER_MSEC.toLong())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        val aspectRatioHeight = 1
        val aspectRatioWidth = 1
        val calculatedHeight = maxWidth * aspectRatioHeight / aspectRatioWidth
        val finalWidth: Int
        val finalHeight: Int
        if (calculatedHeight > maxHeight) {
            finalWidth = maxHeight * aspectRatioWidth / aspectRatioHeight
            finalHeight = maxHeight
        } else {
            finalWidth = maxWidth
            finalHeight = calculatedHeight
        }
        setMeasuredDimension(finalWidth, finalHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        tileSize = mWidth / 14f
        updateEntities()
    }

    private fun updateEntities() {
        for (e in entities) {
            e.setTileSize(tileSize)
            e.setHeight(mHeight)
            e.setWidth(mWidth)
            if (e is Ashman) {
                e.setVelocity(tileSize / 4)
            } else {
                val v = if (level >= 2) 4 else 6
                e.setVelocity(tileSize / v)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        pause()
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.stateToSave = 1
        ss.maze = maze
        ss.cakes = cakes
        ss.level = level
        ss.ghosts = ghosts
        ss.ghostIncrementer = ghostIncrementer
        ss.entities = entities
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
        val ss = state as SavedState
        maze = ss.maze
        cakes = ss.cakes
        level = ss.level
        ghosts = ss.ghosts
        ghostIncrementer = ss.ghostIncrementer
        entities = ss.entities
        play()
    }

    /**
     * http://stackoverflow.com/questions/3542333/how-to-prevent-custom-views-from-losing-state-across-screen-orientation-changes
     * I have orientation locked though, so I don't think this actually does anything..
     */
    internal class SavedState : View.BaseSavedState {
        var stateToSave: Int = 0
        lateinit var maze: Array<Array<String>>
        var cakes = 121
        var level = 1
        var ghosts: Int = 0
        var ghostIncrementer: Int = 0
        lateinit var entities: Array<Entity>

        constructor(superState: Parcelable) : super(superState) {}

        private constructor(`in`: Parcel) : super(`in`) {
            this.stateToSave = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(this.stateToSave)
        }

        companion object {

            //required field that makes Parcelables from a Parcel
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        //Timer stuff
        private const val TIMER_MSEC = 50
    }
}
