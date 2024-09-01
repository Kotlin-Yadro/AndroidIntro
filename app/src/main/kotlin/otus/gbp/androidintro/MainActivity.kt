package otus.gbp.androidintro

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import otus.gbp.androidintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            fun hasMessage(): Boolean = true == intentMessage.text?.toString()?.isNotBlank()

            intentMessage.doAfterTextChanged {
                toInflated.isEnabled = hasMessage()
            }
            toInflated.isEnabled = hasMessage()

            toInflated.setOnClickListener {
                startActivity(
                    InflatedActivity.getIntent(
                        this@MainActivity,
                        intentMessage.text.toString()
                    )
                )
            }
        }
    }
}