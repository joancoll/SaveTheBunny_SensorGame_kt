package cat.dam.andy.savethebunny_sensorgame_kt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.Random

class Egg(context: Context) {
    var egg: Array<Bitmap> = Array(1) { BitmapFactory.decodeResource(context.resources, R.drawable.easter_egg) }
    //egg[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.easter_egg1);
    //egg[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.easter_egg3);
    var spikeFrame = 0
    var spikeX = 0
    var spikeY = 0
    var spikeVelocity = 0
    var random: Random = Random()

    init {
        resetPosition()
    }

    fun getEgg(EggFrame: Int): Bitmap {
        return egg[0]
    }

    val eggWidth: Int
        get() = egg[0].width

    val eggHeight: Int
        get() = egg[0].height

    fun resetPosition() {
        println("GameView.dWidth: " + GameView.dWidth)
        println("eggWidth: " + eggWidth)
        spikeX = random.nextInt(GameView.dWidth - eggWidth)
        spikeY = -200 + random.nextInt(600) * -1
        spikeVelocity = 20 + random.nextInt(16)
    }
}