package otus.gbp.androidintro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class DynamicActivity : AppCompatActivity(R.layout.activity_inflated) {
    companion object {
        private const val EXTRA_MESSAGE = "message"

        fun getIntent(context: Context, message: String): Intent =
            Intent(context, DynamicActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }

        private val Intent.message: String
            get() = extras?.getString(EXTRA_MESSAGE) ?: throw IllegalArgumentException("Message not provided!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createIntentText()
    }

    private fun createIntentText() {
        val id = View.generateViewId()
        val textView = TextView(this).apply {
            setId(id)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            text = intent.message
        }
        layout.addView(textView, 0)

        val constrains = ConstraintSet().apply {
            clone(layout)
            connect(
                id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )
            connect(
                id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )
            connect(
                id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            connect(
                id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
        }
        textView.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        constrains.applyTo(layout)
    }

    private val layout: ConstraintLayout get() = findViewById(R.id.main)
}