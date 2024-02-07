package cat.dam.andy.savethebunny_sensorgame_kt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Explosion(context: Context) {
    var explosion: Array<Bitmap> = Array(4) { index ->
        when (index) {
            0 -> BitmapFactory.decodeResource(context.resources, R.drawable.explosion0)
            1 -> BitmapFactory.decodeResource(context.resources, R.drawable.explosion1)
            2 -> BitmapFactory.decodeResource(context.resources, R.drawable.explosion2)
            3 -> BitmapFactory.decodeResource(context.resources, R.drawable.explosion3)
            else -> throw IllegalArgumentException("Invalid index in array initialization")
        }
    }
    var explosionFrame = 0
    var explosionY = 0
    var explosionX = 0

    init {
    }

    fun getExplosion(explosionFrame: Int): Bitmap {
        return explosion[explosionFrame]
    }
}