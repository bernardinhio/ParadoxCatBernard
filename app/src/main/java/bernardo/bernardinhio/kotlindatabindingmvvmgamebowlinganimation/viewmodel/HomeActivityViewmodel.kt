package bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.viewmodel

import android.content.Intent
import android.view.View
import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.view.TrackingScoreActivityView

class HomeActivityViewmodel(
        var teamOneName : String = "",
        var teamTwoName : String = ""
){

    companion object {
        val TEAM_ONE_NAME = "teamOneName"
        val TEAM_TWO_NAME = "teamTwoName"
    }

    fun startTrackingBowlingGame(view : View){

        // create intent and put extra the names of teams to TrackingScoreActivityView
        val intent = Intent(view.context, TrackingScoreActivityView::class.java)
        intent.putExtra(TEAM_ONE_NAME, teamOneName)
        intent.putExtra(TEAM_TWO_NAME, teamTwoName)

        view.context.startActivities(arrayOf(intent))
    }
}
