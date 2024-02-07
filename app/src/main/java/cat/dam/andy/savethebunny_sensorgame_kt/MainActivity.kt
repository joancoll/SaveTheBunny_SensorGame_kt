package cat.dam.andy.savethebunny_sensorgame_kt

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var btn_start: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        btn_start = findViewById<Button>(R.id.btn_start)
        btn_start.setOnClickListener(View.OnClickListener { v: View? ->
            val gameView = GameView(this)
            setContentView(gameView)
        })
    }
}