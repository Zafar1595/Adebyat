package space.adebyat.adebyat.ui.creation.creation_window

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import space.adebyat.adebyat.R

class CreationWindowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_window)
        Toast.makeText(this, intent.getStringExtra("Creation"), Toast.LENGTH_LONG).show()
    }
}