package space.adebyat.adebyat.ui.author

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import space.adebyat.adebyat.R
import space.adebyat.adebyat.databinding.ActivityAuthorInfoBinding
import space.adebyat.adebyat.databinding.ActivityCreationWindowBinding

class AuthorInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthorInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setData()
    }

    private fun setData(){
        val bio = intent.getStringExtra("bio")
        val imageUrl = intent.getStringExtra("imageUrl")
        binding.textViewAuthorBio.text = bio?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        if (imageUrl != "") {
            Glide.with(this).load(imageUrl).into(binding.imageViewAuthorImage)
        }
    }

}