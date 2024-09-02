package otus.gbp.androidintro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import otus.gbp.androidintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            appMessage.setText(withApp { data.message })
            setAppMessage.setOnClickListener {
                withApp {
                    data = data.copy(message = appMessage.text.toString())
                }
            }

            fun hasMessage(): Boolean = true == intentMessage.text?.toString()?.isNotBlank()

            intentMessage.doAfterTextChanged {
                toInflated.isEnabled = hasMessage()
                toDynamic.isEnabled = hasMessage()
            }
            toInflated.isEnabled = hasMessage()
            toDynamic.isEnabled = hasMessage()

            toInflated.setOnClickListener {
                startActivity(
                    InflatedActivity.getIntent(
                        this@MainActivity,
                        intentMessage.text.toString()
                    )
                )
            }
            toDynamic.setOnClickListener {
                startActivity(
                    DynamicActivity.getIntent(
                        this@MainActivity,
                        intentMessage.text.toString()
                    )
                )
            }
            toMail.setOnClickListener {
                val mailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("jan@example.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                    putExtra(Intent.EXTRA_TEXT, "Email message text")
                }

                startActivity(mailIntent)
            }
        }
    }
}