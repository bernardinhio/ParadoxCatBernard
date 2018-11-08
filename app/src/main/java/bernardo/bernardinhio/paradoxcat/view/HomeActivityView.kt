package bernardo.bernardinhio.paradoxcat.view

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

import bernardo.bernardinhio.paradoxcat.R
import bernardo.bernardinhio.paradoxcat.databinding.ActivityHomeBinding
import bernardo.bernardinhio.paradoxcat.viewmodel.HomeActivityViewmodel

class HomeActivityView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title = "ParadoxCat - Bernard's app"

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
    }
}
