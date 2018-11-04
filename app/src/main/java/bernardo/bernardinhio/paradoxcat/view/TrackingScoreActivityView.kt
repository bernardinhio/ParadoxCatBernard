package bernardo.bernardinhio.paradoxcat.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast

import bernardo.bernardinhio.paradoxcat.R
import bernardo.bernardinhio.paradoxcat.databinding.ActivityTrackingScoreBinding
import bernardo.bernardinhio.paradoxcat.viewmodel.HomeActivityViewmodel
import bernardo.bernardinhio.paradoxcat.viewmodel.TrackingScoreActivityViewmodel

class TrackingScoreActivityView : AppCompatActivity() {

    private var teamOneName: String? = null
    private var teamTwoName: String? = null
    private val shouldAllowBack = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_score)

        val intent = this.intent
        if (intent != null) {
            teamOneName = intent.getStringExtra(HomeActivityViewmodel.TEAM_ONE_NAME)
            teamTwoName = intent.getStringExtra(HomeActivityViewmodel.TEAM_TWO_NAME)
        }

        // initialize the auto-generated databinded Class from the layout used to inflate this view using DataBindingUtil
        val activityTrackingScoreBinding = DataBindingUtil.setContentView<ActivityTrackingScoreBinding>(this@TrackingScoreActivityView, R.layout.activity_tracking_score)

        // create a viewmodel object
        val trackingScoreActivityViewmodel = TrackingScoreActivityViewmodel()

        // use the auto-generated setter of the object inside the tag <variable> in the XML to set the viewModel of that Layout
        activityTrackingScoreBinding.trackingScoreActivityViewmodel = trackingScoreActivityViewmodel

        // close keyboard onStrat
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        title = "Bernard"

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // set click listeners on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.menu_end_game -> {
            }
            R.id.menu_share_app -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // don't allow back before ending the game
    override fun onBackPressed() {
        if (!shouldAllowBack) {
            Toast.makeText(this, "End the game first", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }
}
