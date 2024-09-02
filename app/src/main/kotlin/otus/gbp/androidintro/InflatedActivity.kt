package otus.gbp.androidintro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import otus.gbp.androidintro.databinding.ActivityInflatedBinding
import otus.gbp.androidintro.databinding.ActivityMainBinding

class InflatedActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_MESSAGE = "message"

        fun getIntent(context: Context, message: String): Intent =
            Intent(context, InflatedActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }

        private val Intent.message: String
            get() = extras?.getString(EXTRA_MESSAGE) ?: throw IllegalArgumentException("Message not provided!")
    }

    private lateinit var binding: ActivityInflatedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInflatedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            intentText.text = intent.message
            appText.text = withApp { data.message }
        }
    }
}