package cat.dam.andy.savethebunny_sensorgame_kt

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    lateinit var tvPoints: TextView
    lateinit var tvHighest: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var ivNewHighest: ImageView
    lateinit var Ibtn_restart: ImageButton
    lateinit var Ibtn_exit: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
        tvPoints = findViewById<TextView>(R.id.tv_points)
        tvHighest = findViewById<TextView>(R.id.tv_highest)
        ivNewHighest = findViewById<ImageView>(R.id.ivNewHighest)
        Ibtn_exit = findViewById<ImageButton>(R.id.btn_exit)
        Ibtn_restart = findViewById<ImageButton>(R.id.btn_restart)
        val points = intent.extras!!.getInt("points")
        tvPoints.setText("" + points)
        sharedPreferences = getSharedPreferences("my_pref", 0)
        var highest = sharedPreferences.getInt("highest", 0)
        if (points > highest) {
            ivNewHighest.setVisibility(View.VISIBLE)
            highest = points
            val editor = sharedPreferences.edit()
            editor.putInt("highest", highest)
            editor.commit()
        }
        tvHighest.setText("" + highest)
        Ibtn_restart.setOnClickListener(View.OnClickListener { v: View? -> restart() })
        Ibtn_exit.setOnClickListener(View.OnClickListener { v: View? -> exit() })
    }

    fun restart() {
        val intent = Intent(this@GameOver, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun exit() {
        finish()
    }
}