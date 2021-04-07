package space.adebyat.adebyat.ui.creation.creation_window

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.databinding.ActivityCreationWindowBinding
import java.io.Serializable
import java.lang.Runnable

class CreationWindowActivity : AppCompatActivity(){

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
        setData(creationName, creationContent, creationUrl)
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

    private fun progressBarSetPosition(){
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
                    var msg = Message()
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
            var elapsedTime = createTimeLabel(currentPosition)
            binding.elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            binding.remainingTimeLabel.text = "-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

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

    private fun setLoadingPlaying(loading: Boolean){
        if(loading){
            binding.progressBarPlaying.visibility = View.VISIBLE
            binding.playBtn.visibility = View.GONE
        }else{
            binding.progressBarPlaying.visibility = View.GONE
            binding.playBtn.visibility = View.VISIBLE
        }
    }

    private fun setData(name: String, content: String, url: String) {
        setLoading(false)
        binding.textViewCreationName.text = name
        binding.textViewCreationText.text = content

        if (url == ""){
            binding.exoContainer.visibility = View.GONE
        }else {
            binding.exoContainer.visibility = View.VISIBLE
            mediaPlayerInitialization(url)
            setLoadingPlaying(true)
        }
    }

    fun setLoading(loading: Boolean) {
        if(loading) {
            binding.progressBarAuthor.visibility = View.VISIBLE
        }else{
            binding.progressBarAuthor.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mediaScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaScope.cancel()
    }

}