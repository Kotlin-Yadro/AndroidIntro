package otus.gbp.androidintro

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import otus.gbp.androidintro.databinding.ActivityServiceBinding
import java.lang.ref.WeakReference


class ServiceActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"

        fun getIntent(context: Context): Intent = Intent(context, ServiceActivity::class.java)
    }

    private lateinit var binding: ActivityServiceBinding

    // Messenger for sending messages to the bound service
    private var messenger: WeakReference<Messenger>? = null
    // Messenger for receiving messages from the service
    private val incomingMessenger = Messenger(IncomingHandler(WeakReference(this)))
    // Flag indicating whether we have called bind on the service
    private var bound: Boolean = false

    // Service connection for binding to the service
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            bound = true
            // Register message channel client -> service
            messenger = WeakReference(Messenger(service))
            val message = Message.obtain(null, PlayerService.MSG_REGISTER).apply {
                // Set the reply messenger to receive messages server -> client
                replyTo = incomingMessenger
            }
            // Register client
            messenger?.get()?.send(message)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
            messenger = null
        }
    }

    // Handler of incoming messages from service
    // Server sends us location updates
    private class IncomingHandler(private val activity: WeakReference<ServiceActivity>) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PlayerService.MSG_TEXT -> {
                    val text = msg.data.getString(PlayerService.KEY_TEXT) ?: "No lyrics!"
                    Log.i(TAG, text)
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            buttonBoundService.setOnClickListener {
                if (bound) {
                    enough()
                    buttonBoundService.text = getString(R.string.btn_listen)
                } else {
                    listen()
                    buttonBoundService.text = getString(R.string.btn_enough)
                }
            }

            startService.setOnClickListener {
                checkNotificationPermission()
            }
            stopService.setOnClickListener {
                stopService()
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS).let { result ->
                val granted = PackageManager.PERMISSION_GRANTED == result
                if (granted) {
                    startService()
                } else {
                    notificationPermissionLauncher.launch(POST_NOTIFICATIONS)
                }
            }
        } else {
            startService()
        }
    }

    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        startService()
    }

    override fun onStop() {
        super.onStop()
        enough()
        Log.i(TAG,"Activity stopped!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"Activity destroyed!")
    }

    // Start location tracking service which will run in the foreground
    private fun startService() {
        Log.i(TAG, "Starting player service")
        val intent = Intent(this, PlayerService::class.java)
        startForegroundService(intent)
    }

    // Stop location tracking service
    private fun stopService() {
        Log.i(TAG, "Stopping player service")
        enough()
        val intent = Intent(this, PlayerService::class.java)
        stopService(intent)
    }

    // Listen to the music
    private fun listen() {
        Log.i(TAG, "Listening...")
        if (bound) {
            return
        }
        val intent = Intent(this, PlayerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    // Stop listening to the music
    private fun enough() {
        Log.i(TAG, "Stopping listening...")
        if (bound.not()) {
            return
        }
        val message = Message.obtain(null, PlayerService.MSG_UNREGISTER)
        messenger?.get()?.send(message)

        unbindService(connection)
        bound = false
        messenger = null
    }
}