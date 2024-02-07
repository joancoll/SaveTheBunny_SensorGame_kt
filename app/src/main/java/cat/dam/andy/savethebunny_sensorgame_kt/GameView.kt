package cat.dam.andy.savethebunny_sensorgame_kt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.view.View
import android.widget.Toast
import java.util.Random

class GameView(var gameContext: Context) : View(gameContext) {
    var background: Bitmap
    var ground: Bitmap
    var rabbit: Bitmap
    var rectBackground: Rect
    var rectGround: Rect
    var gameHandler: Handler
    var sensorManager: SensorManager
    var gyroscopeSensor: Sensor?
    var gyroscopeEventListener: SensorEventListener? = null
    val UPDATE_MILIS: Long = 30
    var runnable: Runnable
    var textPaint = Paint()
    var healthPaint = Paint()
    var TEXT_SIZE = 120f
    var points = 0
    var life = 3
    var random: Random
    var rabbitX: Float
    var rabbitY: Float
    var oldX = 0f
    var oldRabbitX = 0f
    var eggs: ArrayList<Egg>
    var explosions: ArrayList<Explosion>
    private val gameOverIntent = Intent(gameContext, GameOver::class.java)

    init {
        background = BitmapFactory.decodeResource(resources, R.drawable.background)
        ground = BitmapFactory.decodeResource(resources, R.drawable.ground)
        rabbit = BitmapFactory.decodeResource(resources, R.drawable.rabbit)
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        rectBackground = Rect(0, 0, dWidth, dHeight)
        rectGround = Rect(0, dHeight - ground.height, dWidth, dHeight)
        gameHandler = Handler()
        runnable = Runnable { invalidate() }
        textPaint.color = Color.rgb(255, 165, 0)
        textPaint.textSize = TEXT_SIZE
        textPaint.textAlign = Paint.Align.LEFT
        healthPaint.color = Color.GREEN
        random = Random()
        rabbitX = (dWidth / 2 - rabbit.width / 2).toFloat()
        rabbitY = (dHeight - ground.height - rabbit.height).toFloat()
        eggs = ArrayList<Egg>()
        explosions = ArrayList<Explosion>()
        for (i in 0..2) {
            val egg = Egg(context)
            eggs.add(egg)
        }
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (gyroscopeSensor == null) {
            Toast.makeText(context, "The device has no Gyroscope.", Toast.LENGTH_SHORT).show()
        }
        val gyroscopeEventListener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.values[2] > 0.5f) {
                    rabbitX += 4f
                } else if (event.values[2] < -0.5f) {
                    rabbitX -= 4f
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }
        sensorManager.registerListener(
            gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(background, null, rectBackground, null)
        canvas.drawBitmap(ground, null, rectGround, null)
        canvas.drawBitmap(rabbit, rabbitX, rabbitY, null)
        for (i in eggs.indices) {
            canvas.drawBitmap(
                eggs[i].getEgg(eggs[i].spikeFrame),
                eggs[i].spikeX.toFloat(),
                eggs[i].spikeY.toFloat(),
                null
            )
            eggs[i].spikeFrame++
            if (eggs[i].spikeFrame > 2) {
                eggs[i].spikeFrame = 0
            }
            eggs[i].spikeY += eggs[i].spikeVelocity
            if (eggs[i].spikeY + eggs[i].eggHeight >= dHeight - ground.height) {
                points += 10
                val explosion = Explosion(gameContext)
                explosion.explosionX = eggs[i].spikeX
                explosion.explosionY = eggs[i].spikeY
                explosions.add(explosion)
                eggs[i].resetPosition()
            }
        }
        for (i in eggs.indices) {
            if (eggs[i].spikeX + eggs[i].eggWidth >= rabbitX && eggs[i].spikeX <= rabbitX + rabbit.width && eggs[i].spikeY + eggs[i].eggWidth >= rabbitY && eggs[i].spikeY + eggs[i].eggWidth <= rabbitY + rabbit.height) {
                life--
                eggs[i].resetPosition()
                if (life == 0) {
                    val intent = Intent(gameContext, GameOver::class.java)
                    intent.putExtra("points", points)
                    gameContext.startActivity(intent)
                    (gameContext as Activity).finish()
                }
            }
        }
        for (i in explosions.indices-1) {
            canvas.drawBitmap(
                explosions[i].getExplosion(explosions[i].explosionFrame),
                explosions[i].explosionX.toFloat(),
                explosions[i].explosionY.toFloat(),
                null
            )
            explosions[i].explosionFrame++
            if (explosions[i].explosionFrame > 3) {
                explosions.removeAt(i)
            }
        }
        if (life == 2) {
            healthPaint.color = Color.YELLOW
        } else if (life == 1) {
            healthPaint.color = Color.RED
        }
        canvas.drawRect(
            (dWidth - 200).toFloat(),
            30f,
            (dWidth - 200 + 60 * life).toFloat(),
            80f,
            healthPaint
        )
        //Score TEXT
        //canvas.drawText(""+points,20, TEXT_SIZE, TextPaint);
        gameHandler.postDelayed(runnable, UPDATE_MILIS)
    }

    companion object {
        var dWidth: Int = 0
        var dHeight: Int = 0
    }
}