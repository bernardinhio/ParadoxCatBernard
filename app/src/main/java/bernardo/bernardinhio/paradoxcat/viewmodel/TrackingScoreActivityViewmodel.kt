package bernardo.bernardinhio.paradoxcat.viewmodel

import android.databinding.BaseObservable
import android.text.Editable
import android.view.View
import android.widget.Toast
import bernardo.bernardinhio.paradoxcat.Frame
import bernardo.bernardinhio.paradoxcat.model.TrackingScoreActivityModel
import java.util.*

class TrackingScoreActivityViewmodel(
        val teamOneName : String = "",
        val teamTwoName : String = "",

        var currentTeam: Int = 1, // either 1 or 2

        var firstRoll: String = "",
        var firstRollInfo: String = "", // "is bonus to Frame X"
        var firstRollEnabled : Boolean = true,
        var firstRollFinished : Boolean = false,

        var secondRoll: String = "",
        var secondRollInfo: String = "", // is bonus to Frame X
        var secondRollEnabled : Boolean = false,
        var secondRollRightIsGivenToPlayer : Boolean = false,
        var secondRollFinished : Boolean = false,

        var submitEnabled : Boolean = false,
        var textSurfToOtherTeam: String = "Save the 1st Roll \nand continue the game !",
        var opponentTeamTotalScore : String = "0",

        var frameCompleted : String = "", // "Not completed"
        var frameCategory : String = "", // "Strike"
        var currentTeamFrameScore : String = "0",
        var currentTeamTotalScore : String = "0",
        var currentTeamFrameNumber : String = "1",
        var currentFrameInfoNumberAndScore: String = "Frame # ------> Score: 0",

        var firstBonusReceived : String = "", // "Bonus: +X from Frame Y"
        var secondBonusReceived : String = "" // Bonus: +X from Frame Y

): Observer, BaseObservable(){

    val teamOneFramesList : ArrayList<Frame> = ArrayList()
    val teamTwoFramesList : ArrayList<Frame> = ArrayList()

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

    fun afterTextChangedFirstRoll(editable : Editable){

        // game starts with Roll 2 disabled
        if (!secondRollEnabled ){
            // first time activity appears
            if (!firstRoll.isEmpty()){
                when(firstRoll.toInt()){
                    in 0..9 -> {
                        firstRollInfo = "Oh notot all of the Pins! \nLater you try 2nd Roll !"
                        submitEnabled = true
                    }
                    10 -> {
                        firstRollInfo = "Nice STRIKE !! \n10 Pins in 1 Roll !"
                        submitEnabled = true
                    }
                    in 11..99 -> {
                        firstRollInfo = "can't be above 10 !"
                        submitEnabled = false
                    }
                }
            } else {
                firstRollInfo = "How many Pins you hit ?"
                submitEnabled = false
            }
            this.notifyChange()
        }
    }


    fun submitEntry(view : View){
        // After the first Roll is finished and its value is valid between 0 and 10
        if (!secondRollEnabled && firstRoll.toInt()in 0..10){

            when(firstRoll.toInt()){
                10 -> {

                    // disabled Roll 1 we don't need it anymore
                    firstRollEnabled = false
                    firstRollFinished = true

                    // when the first Roll is Strike then give turn to other team
                    textSurfToOtherTeam = "Save this Frame $currentTeamFrameNumber \nand go to next team $teamTwoName"
                    submitEnabled = true

                    // disable Roll 2 because don't need it anymore but show it with its info
                    secondRollRightIsGivenToPlayer = true
                    secondRollEnabled = false
                    secondRollFinished = true
                    secondRollInfo = "<-- not needed"

                    // calculate & show result for current score
                    frameCompleted = "Is completed !"
                    frameCategory = "Strike"
                    currentTeamFrameScore = "10"
                    currentFrameInfoNumberAndScore= "Frame $currentTeamFrameNumber ------> Score: $currentTeamFrameScore"

                    // calculate & show total score for current team
                    currentTeamTotalScore = currentTeamFrameScore

                    this.notifyChange()

                    // go to next player
                    Toast.makeText(view.context,
                            "Save Roll 1 \n Go to other Player",
                            Toast.LENGTH_SHORT).show()
                }
                in 0..9 -> {

                    // after Roll 1 finished but player didn't hit all Pins, indicate to player to give turn to next player
                    textSurfToOtherTeam = "Enter 2nd Roll for Frame $currentTeamFrameNumber \nand continue the game !"
                    // Enable this button after player enters the right input for Roll 2
                    submitEnabled = false

                    // disactivate Roll 1 we don't need it anymore
                    firstRollEnabled = false
                    firstRollFinished = true
                    firstRollInfo = ""

                    // show Roll 2
                    secondRollRightIsGivenToPlayer = true
                    secondRollEnabled = true
                    secondRollFinished = false
                    secondRollInfo = "How many Pins you hit ?"

                    this.notifyChange()

                    // now the player enters the score for the 2nd frame
                    Toast.makeText(view.context,
                            "ENTER 2nd Roll",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }

        // after the player enter the 2nd Roll and has already saved & entered the 1st Roll
        if (secondRollEnabled && !secondRoll.isEmpty() && !firstRollEnabled){

            // keep Roll 2 but disable it
            secondRollRightIsGivenToPlayer = true
            secondRollEnabled = false
            secondRollFinished = true
            secondRollInfo = ""

            // calculate & show result for current score
            frameCompleted = "Is completed !"
            currentTeamFrameScore = (firstRoll.toInt() + secondRoll.toInt()).toString()
            frameCategory = if (currentTeamFrameScore.toInt() == 10)"Spare" else "Open"
            currentFrameInfoNumberAndScore= "Frame $currentTeamFrameNumber ------> Score: $currentTeamFrameScore"

            // calculate & show total score for current team
            currentTeamTotalScore = currentTeamFrameScore
            // after the 2nd Rolls is enter and user clicks on Button
            textSurfToOtherTeam = "Save this Frame $currentTeamFrameNumber \nand go to next team $teamOneName"
            submitEnabled = true

            this.notifyChange()

            // now player gives turn to other Player
            Toast.makeText(view.context,
                    "2nd Roll completed \n1st Roll completed \ngo to Next playerl",
                    Toast.LENGTH_SHORT).show()
        }
    }



    fun afterTextChangedSecondRoll(editable : Editable){
        // after the first roll is finished
        if (!firstRollEnabled && firstRollFinished){
            // first time Roll 2 is used
            if (!secondRoll.isEmpty()){
                val remainingPins = 10 - firstRoll.toInt()
                when(secondRoll.toInt()){
                    in 0..(remainingPins - 1) -> {
                        secondRollInfo = "Didn't get all the Pins in \n2 Rolls? Bad luck !"
                        // after the 2nd Rolls is enter
                        textSurfToOtherTeam = "Save 2nd Roll and save this Frame $currentTeamFrameNumber"
                        submitEnabled = true
                    }
                    remainingPins -> {
                        secondRollInfo = "Nice SPARE !! \n10 Pins in 2 Rolls!"
                        textSurfToOtherTeam = "Save 2nd Roll and save this Frame $currentTeamFrameNumber"
                        submitEnabled = true
                    }
                    in (remainingPins + 1)..99 -> {
                        secondRollInfo = "can't be above $remainingPins !"
                        textSurfToOtherTeam = "Enter the 2nd Roll for Frame $currentTeamFrameNumber \nand continue the game !"
                        submitEnabled = false
                    }
                }
            } else {
                secondRollInfo = "How many Pins you hit?"
                textSurfToOtherTeam = "Enter the 2nd Roll for Frame $currentTeamFrameNumber \nand continue the game !"
                submitEnabled = false
            }
            this.notifyChange()
        }
    }




}
