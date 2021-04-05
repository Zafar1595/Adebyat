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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import space.adebyat.adebyat.R
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.databinding.ActivityCreationWindowBinding

class CreationWindowActivity : AppCompatActivity(), CreationWindowModelView {


    lateinit var binding: ActivityCreationWindowBinding
    private val mediaJob = Job()
    private val mediaScope = CoroutineScope(Dispatchers.Main + mediaJob)
    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0
    private var url = ""//"https://firebasestorage.googleapis.com/v0/b/my-first-project-in-fire-e3bc0.appspot.com/o/allmusics%2F5sta%20Family%20-%20%D0%92%D0%BC%D0%B5%D1%81%D1%82%D0%B5%20%D0%9C%D1%8B.mp3?alt=media&token=a9a639c9-4930-47c6-baba-f7fd824a1c3d"
    private var isPlay = false
    private val presenter: CreationWindowPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreationWindowBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var creationName = intent.getStringExtra("Creation")!!
        presenter.init(this)
        presenter.getCreation(creationName)


    }

    private fun mediaPlayerInitialization() = mediaScope.launch(Dispatchers.IO) {
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

    override fun setData(creation: Creation) {
        setLoading(false)
        binding.textViewCreationName.text = creation.name
        binding.textViewCreationText.text = creation.content
        url = creation.audioUrl
        if (url == ""){
            binding.exoContainer.visibility = View.GONE
        }else {
            binding.exoContainer.visibility = View.VISIBLE
            mediaPlayerInitialization()
            setLoadingPlaying(true)
        }
    }

    override fun setLoading(loading: Boolean) {
        if(loading) {
            binding.progressBarAuthor.visibility = View.VISIBLE
        }else{
            binding.progressBarAuthor.visibility = View.GONE
        }
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}