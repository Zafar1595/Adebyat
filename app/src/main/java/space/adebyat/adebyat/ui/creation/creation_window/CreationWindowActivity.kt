package space.adebyat.adebyat.ui.creation.creation_window

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import space.adebyat.adebyat.databinding.ActivityCreationWindowBinding

class CreationWindowActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreationWindowBinding

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreationWindowBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setLoading(true)
        val creationName = intent.getStringExtra("creationName")!!
        val creationContent = intent.getStringExtra("creationContent")!!
        val creationUrl = intent.getStringExtra("creationUrl")!!
        STREAM_URL = creationUrl
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

    private fun setData(name: String, content: String, url: String, isConnected: Boolean) {
        setLoading(false)
        binding.textViewCreationName.text = name
        binding.textViewCreationText.text = content

        if (url != "" && isConnected) {
            binding.playerView.visibility = View.VISIBLE
        } else {
            binding.playerView.visibility = View.GONE
        }
    }

    fun setLoading(loading: Boolean) {
        if (loading) {
            binding.progressBarAuthor.visibility = View.VISIBLE
        } else {
            binding.progressBarAuthor.visibility = View.GONE
        }
    }

    private fun initializePlayer() {

        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                MediaItem.fromUri(STREAM_URL))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = SimpleExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()

        simpleExoPlayer.addMediaSource(mediaSource)

        simpleExoPlayer.playWhenReady = true

        binding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        binding.playerView.player = simpleExoPlayer
        binding.playerView.requestFocus()
    }

    private fun releasePlayer() {
        simpleExoPlayer.release()
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

    companion object {
        var STREAM_URL = ""
    }
}