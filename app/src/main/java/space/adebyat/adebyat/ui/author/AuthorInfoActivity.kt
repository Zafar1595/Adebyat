package space.adebyat.adebyat.ui.author

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    fun setData(){
        var bio = intent.getStringExtra("bio")
        var imageUrl = intent.getStringExtra("imageUrl")
        binding.textViewAuthorBio.text = bio
        if (imageUrl != "") {
            Glide.with(this).load(imageUrl).into(binding.imageViewAuthorImage)
        }
    }

}