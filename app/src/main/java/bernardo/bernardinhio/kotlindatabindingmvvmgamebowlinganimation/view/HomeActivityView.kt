package bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.view

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.WindowManager
import android.widget.TextView

import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.R
import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.databinding.ActivityHomeBinding
import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.viewmodel.HomeActivityViewmodel

class HomeActivityView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title = "Score hitting Pins"

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        // set some background
        val drawableBackground = ResourcesCompat.getDrawable(this.resources, R.drawable.bowling_background, null)
        drawableBackground!!.alpha = 255
        drawableBackground.colorFilter = PorterDuffColorFilter(Color.parseColor("#80FFFFFF"), PorterDuff.Mode.SCREEN) // 80% transparent
        this.window.setBackgroundDrawable(drawableBackground)

        // initialize the auto-generated databinded Class from the layout used to inflate this view using DataBindingUtil
        val activityHomeBinding = DataBindingUtil.setContentView<ActivityHomeBinding>(this@HomeActivityView, R.layout.activity_home)

        // create a viewmodel object
        val viewModel = HomeActivityViewmodel()

        // use the auto-generated setter of the object inside the tag <variable> in the XML to set the viewModel of that Layout
        activityHomeBinding.viewModel = viewModel

        // created formatted text with clcikable span
        val textView = findViewById<TextView>(R.id.tv_welcome)
        val fullText = resources.getText(R.string.welcome_home).toString()
        val readMore = resources.getText(R.string.read_more).toString()

        // set the text equals to the formatted one that than clickable span
        val viewTreeObserver = textView.viewTreeObserver

        viewTreeObserver.addOnGlobalLayoutListener { this.setTextFormatted(textView, fullText, readMore, 3) }
    }

    private fun setTextFormatted(textView: TextView, fullText: String, linkLabel: String, maxLines: Int) {
        var formattedText = ""
        val indexLineEnd = textView.layout.getLineEnd(maxLines - 1)

        formattedText = (textView.text.subSequence(
                0,
                indexLineEnd - "... ".length - linkLabel.length).toString()
                + "... "
                + linkLabel)

        val clickableSpan = object : ClickableSpan() {

            override fun onClick(view: View) {
                val intent : Intent = Intent(view.context, ReadAboutBowlingGameActivity::class.java)
                view.context.startActivities(arrayOf(intent))
            }
        }

        val spannableStringBuilder = SpannableStringBuilder(formattedText)

        spannableStringBuilder.setSpan(
                clickableSpan,
                formattedText.indexOf(linkLabel) + 1, // + 1 is the "("
                formattedText.indexOf(linkLabel) + linkLabel.length - 1, // + 1 is the ")"
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableStringBuilder

        textView.movementMethod = LinkMovementMethod.getInstance()

    }
}
