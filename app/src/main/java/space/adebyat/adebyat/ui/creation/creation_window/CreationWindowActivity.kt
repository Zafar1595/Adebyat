package space.adebyat.adebyat.ui.creation.creation_window

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import space.adebyat.adebyat.R
import space.adebyat.adebyat.databinding.ActivityCreationWindowBinding
import java.lang.Runnable

class CreationWindowActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreationWindowBinding
    private val mediaJob = Job()
    private val mediaScope = CoroutineScope(Dispatchers.Main + mediaJob)
    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreationWindowBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setLoading(true)
        val creationName = intent.getStringExtra("creationName")!!
        val creationContent = intent.getStringExtra("creationContent")!!
        val creationUrl = intent.getStringExtra("creationUrl")!!

        //Проверка интернет соеденения
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            setData(creationName, creationContent, creationUrl, true)
        } else {
            setData(creationName, creationContent, creationUrl, false)
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mediaPlayerInitialization(url: String) = mediaScope.launch(Dispatchers.IO) {
        mp = MediaPlayer().apply {
            setAudioAttributes(
                    AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            )
            setDataSource(url)
            prepare()
        }

        mp.isLooping = true
        totalTime = mp.duration
        runOnUiThread {
            // Stuff that updates the UI
            setLoadingPlaying(false)
            progressBarSetPosition()
        }
    }

    private fun progressBarSetPosition() {
        // Position Bar
        binding.positionBar.max = totalTime
        binding.positionBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            mp.seekTo(progress)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                }
        )
        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    val msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            binding.positionBar.progress = currentPosition

            // Update Labels
            val elapsedTime = createTimeLabel(currentPosition)
            binding.elapsedTimeLabel.text = elapsedTime

            val remainingTime = createTimeLabel(totalTime - currentPosition)
            binding.remainingTimeLabel.text = "-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }

    fun playBtnClick(v: View) {
        if (mp.isPlaying) {
            // Stop
            mp.pause()
            binding.playBtn.setBackgroundResource(R.drawable.play)

        } else {
            // Start
            mp.start()
            binding.playBtn.setBackgroundResource(R.drawable.pause)
        }
    }

    private fun setLoadingPlaying(loading: Boolean) {
        if (loading) {
            binding.progressBarPlaying.visibility = View.VISIBLE
            binding.playBtn.visibility = View.GONE
        } else {
            binding.progressBarPlaying.visibility = View.GONE
            binding.playBtn.visibility = View.VISIBLE
        }
    }

    private fun setData(name: String, content: String, url: String, isConnected: Boolean) {
        setLoading(false)
        binding.textViewCreationName.text = name
        binding.textViewCreationText.text = content

        if (url != "" && isConnected) {
            binding.exoContainer.visibility = View.VISIBLE
            mediaPlayerInitialization(url)
            setLoadingPlaying(true)
        } else {
            binding.exoContainer.visibility = View.GONE
        }
    }

    fun setLoading(loading: Boolean) {
        if (loading) {
            binding.progressBarAuthor.visibility = View.VISIBLE
        } else {
            binding.progressBarAuthor.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.exoContainer.visibility == 0 && binding.playBtn.visibility == 0) {
            if (mp.isPlaying) {
                mp.stop()
                mp.release()
            }
        }
        mediaScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaScope.cancel()
    }

}