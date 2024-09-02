package otus.gbp.androidintro

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StateActivityViewModel : ViewModel() {
    private val _countState = MutableStateFlow(0)
    private var countJob: Job? = null

    val countState: StateFlow<Int> get() = _countState.asStateFlow()

    fun launchTimer() {
        countJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _countState.emit(_countState.value + 1)
            }
        }
    }

    fun stopTimer() {
        countJob?.let {
            it.cancel()
            countJob = null
        }
    }



    override fun onCleared() {
        Log.i(TAG, "Cleared")
    }

    companion object {
        private const val TAG = "StateActivityViewModel"
    }
}