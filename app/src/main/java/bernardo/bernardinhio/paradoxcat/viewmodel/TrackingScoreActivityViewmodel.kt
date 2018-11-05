package bernardo.bernardinhio.paradoxcat.viewmodel

import android.databinding.BaseObservable
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import bernardo.bernardinhio.paradoxcat.Frame
import bernardo.bernardinhio.paradoxcat.model.TrackingScoreActivityModel
import java.util.*

class TrackingScoreActivityViewmodel(
        val currentTeam: Int = 1,
        val currentTeamTotalScore : String = "0",
        val opponentTeamTotalScore : String = "0",
        val currentFrameNumberAndScore: String = "Frame 1 ------> Score: 0",
        var firstRoll: String = "",
        var firstRollInfoOrWasGivenAsBonusToWhichFrame: String = "", // "is bonus to Frame X"
        var showSecondRollEntry : Boolean = false,
        var firstRollEnabled : Boolean = true,
        var secondRollEnabled : Boolean = false,
        var secondRoll: String = "",
        var secondRollInfoAndWasGivenAsBonusToWhichFrame: String = "", // is bonus to Frame X
        var submitEnabled : Boolean = false,
        val frameCompleted : String = "", // "Not completed"
        val frameCategory : String = "", // "Strike"
        val firstBonusReceived : String = "", // "Bonus: +X from Frame Y"
        val secondBonusReceived : String = "", // Bonus: +X from Frame Y
        val textSurfToOtherTeam: String = "Save Frame X \ngo to abcdefg"
): Observer, BaseObservable(){

    override fun update(observable: Observable?, p1: Any?) {
        /**
         * ViewModel is Observer of the Model so once the ViewModel is
         * notified by the Model, then the ViewModel that is also at
         * the same time Observable to the View (XML), should notify
         * that View as well
         * read more https://medium.com/@zhangqichuan/android-development-with-mvvm-and-kotlin-9598c3623ce1
         */
        if (observable is TrackingScoreActivityModel){
            // the ViewModel notifies the View (the xml)
            this.notifyChange()

            // when the specific variables from the model is updated
            if (p1 is String && p1.equals("frameScore")){

            }
        }
    }

    val teamOneFramesList : ArrayList<Frame> = ArrayList()
    val teamTwoFramesList : ArrayList<Frame> = ArrayList()


    fun giveTurnToTheOtherTeam(view : View){
        secondRollEnabled = true
        Log.d("scoring", "submitClicked : secondRollEnabled")
        firstRollEnabled = false
        Log.d("scoring", "submitClicked : firstRollDisabled")
        Toast.makeText(view.context,
                "HELLO $textSurfToOtherTeam ",
                Toast.LENGTH_SHORT).show()
    }

    fun afterTextChangedFirstRoll(editable : Editable){
        Log.d("textChanged", "textChanged")
        if (!firstRoll.isEmpty()){
            when(firstRoll.toInt()){
                in 0..9 -> {
                    Log.d("scoring", "textChanged : showSecondRoll")
                    showSecondRollEntry = true
                    firstRollInfoOrWasGivenAsBonusToWhichFrame = ""
                    submitEnabled = true
                }
                10 -> {
                    Log.d("", "scoring : hideSecondRoll")
                    showSecondRollEntry = false
                    firstRollInfoOrWasGivenAsBonusToWhichFrame = ""
                    submitEnabled = true
                }
                in 11..99 -> {
                    Log.d("scoring", "textChanged : hideSecondRoll")
                    showSecondRollEntry = false
                    firstRollInfoOrWasGivenAsBonusToWhichFrame = "can't be above 10"
                    submitEnabled = false
                }
            }
        } else {
            Log.d("scoring", "textChanged : hideSecondRoll")
            showSecondRollEntry = false
            firstRollInfoOrWasGivenAsBonusToWhichFrame = ""
            submitEnabled = false
        }
        this.notifyChange()
    }
}
