package bernardo.bernardinhio.paradoxcat.viewmodel

import android.view.View
import android.widget.Toast
import bernardo.bernardinhio.paradoxcat.Frame
import bernardo.bernardinhio.paradoxcat.R

class TrackingScoreActivityViewmodel(
        val currentTeam: Int = 1,
        val currentTeamTotalScore : String = "0",
        val opponentTeamTotalScore : String = "0",
        val currentFrameNumberAndScore: String = "Frame 1 ------> Score: 0",
        var firstRoll: String = "",
        val firstRollWasGivenAsBonusToWhichFrame: String = "", // "is bonus to Frame X"
        var secondRoll: String = "",
        val secondRollWasGivenAsBonusToWhichFrame: String = "", // is bonus to Frame X
        val frameCompleted : String = "", // "Not completed"
        val frameCategory : String = "", // "Strike"
        val firstBonusReceived : String = "", // "Bonus: +X from Frame Y"
        val secondBonusReceived : String = "", // Bonus: +X from Frame Y
        val textSurfToOtherTeam: String = "Save Frame X \ngo to abcdefg"
){

    val teamOneFramesList : ArrayList<Frame> = ArrayList()
    val teamTwoFramesList : ArrayList<Frame> = ArrayList()


    fun giveTurnToTheOtherTeam(view : View){
        Toast.makeText(view.context,
                "HELLO $textSurfToOtherTeam ",
                Toast.LENGTH_SHORT).show()
    }
}
