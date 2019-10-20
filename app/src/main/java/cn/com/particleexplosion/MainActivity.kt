package cn.com.particleexplosion

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.com.explosion.ClickCallback
import cn.com.explosion.ExplosionField
import cn.com.explosion.FallingParticleFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val explosionField = ExplosionField(this, FallingParticleFactory())
        explosionField.setClickCallback(clickCallback)
        explosionField.addListener(findViewById(R.id.text))
        explosionField.addListener(findViewById(R.id.image))
        explosionField.addListener(findViewById(R.id.layout))
    }

    private var clickCallback: ClickCallback = object : ClickCallback {
        override fun onClick(v: View) {
           Toast.makeText(this@MainActivity,"点击回调",Toast.LENGTH_SHORT).show()
        }
    }
}
