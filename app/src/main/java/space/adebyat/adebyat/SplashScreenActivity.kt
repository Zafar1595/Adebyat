package space.adebyat.adebyat

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import space.adebyat.adebyat.databinding.ActivitySplashScreenBinding
import space.adebyat.adebyat.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()
        binding.lottieView.setMaxProgress(0.6f)
        binding.lottieView.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })

    }
}