package otus.gbp.androidintro

import android.app.Activity
import android.app.Application

class IntroApp : Application() {
    lateinit var data: IntroAppData

    override fun onCreate() {
        super.onCreate()
        data = IntroAppData("Hello App!")
    }
}

data class IntroAppData(val message: String)

inline fun <T> Activity.withApp(block: IntroApp.() -> T): T {
    return (application as IntroApp).block()
}
