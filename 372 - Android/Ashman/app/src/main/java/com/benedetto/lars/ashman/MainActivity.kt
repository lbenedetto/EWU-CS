package com.benedetto.lars.ashman

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.benedetto.lars.ashman.enums.Direction.DOWN
import com.benedetto.lars.ashman.enums.Direction.LEFT
import com.benedetto.lars.ashman.enums.Direction.RIGHT
import com.benedetto.lars.ashman.enums.Direction.UP

class MainActivity : AppCompatActivity(), CakeListener {
    private lateinit var textViewCakes: TextView
    private lateinit var textViewLevel: TextView
    private lateinit var mazeView: MazeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mazeView = findViewById(R.id.mazeView)
        mazeView.registerCakeListener(this)
        val dpadUp = findViewById<ImageView>(R.id.dpadUp)
        val dpadDown = findViewById<ImageView>(R.id.dpadDown)
        val dpadLeft = findViewById<ImageView>(R.id.dpadLeft)
        val dpadRight = findViewById<ImageView>(R.id.dpadRight)
        dpadUp.setOnClickListener { mazeView.setAshmanDirection(UP) }
        dpadDown.setOnClickListener { mazeView.setAshmanDirection(DOWN) }
        dpadLeft.setOnClickListener { mazeView.setAshmanDirection(LEFT) }
        dpadRight.setOnClickListener { mazeView.setAshmanDirection(RIGHT) }
        val startGame = findViewById<TextView>(R.id.textViewInstructions)
        startGame.setOnClickListener { mazeView.pausePlay() }
        startGame.setOnLongClickListener {
            mazeView.cheat()
            true
        }
        textViewCakes = findViewById(R.id.textViewCakesRemaining)
        textViewLevel = findViewById(R.id.textViewLevel)
        val s = getString(R.string.level) + " " + 1
        textViewLevel.text = s
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_about) {
            Toast.makeText(this, "Ashman, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show()
            return true
        } else if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun updateCakes(cakes: Int) {
        val s = getString(R.string.cakes_left) + cakes
        textViewCakes.text = s
        updateLevel()
    }

    private fun updateLevel() {
        val s = getString(R.string.level) + " " + mazeView.level
        textViewLevel.text = s
    }
}
