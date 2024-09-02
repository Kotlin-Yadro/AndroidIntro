package otus.gbp.androidintro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
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
    private val model: StateActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            startTimer.setOnClickListener {
                model.launchTimer()
            }
            stopTimer.setOnClickListener {
                model.stopTimer()
            }
            lifecycleScope.launch {
                model.countState.collect {
                    timerValue.text = getString(R.string.timer_value, it)
                }
            }
        }
    }
}