package otus.gbp.androidintro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import otus.gbp.androidintro.databinding.ActivityStateBinding

class StateActivity : AppCompatActivity() {
    companion object {
        fun getIntent(context: Context): Intent = Intent(context, StateActivity::class.java)
    }

    private lateinit var binding: ActivityStateBinding
    private var count = 0
    private var countJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayCount()
        with(binding) {
            startTimer.setOnClickListener {
                launchTimer()
            }
            stopTimer.setOnClickListener {
                stopTimer()
            }
        }
    }

    private fun displayCount() = with(binding) {
        timerValue.text = getString(R.string.timer_value, count)
    }

    private fun launchTimer() {
        countJob = lifecycleScope.launch {
            while (isActive) {
                displayCount()
                delay(1000)
                ++count
            }
        }
    }

    private fun stopTimer() {
        countJob?.let {
            it.cancel()
            countJob = null
        }
    }
}