package bernardo.bernardinhio.paradoxcat.viewmodel

import android.view.View
import android.widget.Toast
import bernardo.bernardinhio.paradoxcat.Frame
import bernardo.bernardinhio.paradoxcat.Game
import bernardo.bernardinhio.paradoxcat.Session

class TrackingScoreActivityViewmodel(
        var textSurfToOtherTeam : String = "Save Frame-1 & go to next player"
){

    val teamOneFramesList : ArrayList<Frame> = ArrayList()
    val teamTwoFramesList : ArrayList<Frame> = ArrayList()


    fun giveTurnToTheOtherTeam(view : View){
        Toast.makeText(view.context,
                "HELLO $textSurfToOtherTeam ",
                Toast.LENGTH_SHORT).show()
    }
}
