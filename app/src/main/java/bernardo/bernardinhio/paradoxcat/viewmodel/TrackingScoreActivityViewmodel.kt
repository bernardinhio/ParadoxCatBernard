package bernardo.bernardinhio.paradoxcat.viewmodel

import android.databinding.BaseObservable
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import bernardo.bernardinhio.paradoxcat.Frame
import bernardo.bernardinhio.paradoxcat.model.TrackingScoreActivityModel
import java.util.*
import kotlin.collections.ArrayList

class TrackingScoreActivityViewmodel(
        val teamOneName : String, // fields passed from Launcher Activity
        val teamTwoName : String,

        var activeTeamNumber: Int = 1,
        var opponentTeamNumber: Int = 2,

        var activeTeamTotalScore : String = "0",
        var opponentTeamTotalScore : String = "0",

        var firstRoll: String = "",
        var firstRollInfo: String = "",
        var firstRollEnabled : Boolean = true,
        var firstRollFinished : Boolean = false,

        var secondRoll: String = "",
        var secondRollInfo: String = "",
        var secondRollEnabled : Boolean = false,
        var hasSecondRollRight : Boolean = false,
        var secondRollFinished : Boolean = false,

        var submitEnabled : Boolean = false,
        var messageSubmitButton: String = "Save the 1st Roll \nand continue the game !",

        var currentFrameNumber : Int = 1,
        var frameTitleInfo: String = "Frame 1 ------> Score: 0",
        var frameScore : String = "0",
        var frameCategory : String = "",
        var textIsFrameCompleted : String = "",

        var messageFirstBonus : String = "",
        var messageSecondBonus : String = ""

): Observer, BaseObservable(){

    val teamOneFramesList : ArrayList<Frame> = ArrayList()
    val teamTwoFramesList : ArrayList<Frame> = ArrayList()
    var frameIsCompletedAndSaved : Boolean = false

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
        if (!secondRollEnabled ){
            // after user enters any number to 1st Roll
            if (!firstRoll.isEmpty()){
                when(firstRoll.toInt()){
                    in 0..9 -> {
                        firstRollInfo = "Oh not all of the Pins... \nLater you try 2nd Roll !"
                        submitEnabled = true
                    }
                    10 -> {
                        firstRollInfo = "Nice STRIKE ! \n10 Pins in 1 Roll !"
                        submitEnabled = true
                    }
                    in 11..99 -> { // more than 2 digits not allowed in UI
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


    fun afterTextChangedSecondRoll(editable : Editable){
        // after the first roll is finished
        if (!firstRollEnabled && firstRollFinished){
            // first time Roll 2 is used
            if (!secondRoll.isEmpty()){
                val remainingPins = 10 - firstRoll.toInt()
                when(secondRoll.toInt()){
                    in 0..(remainingPins - 1) -> {
                        secondRollInfo = "Ah.. didn't get them all ...Bad luck !"
                        // after the 2nd Rolls is enter
                        messageSubmitButton = "Save 2nd Roll and calculate score after this Frame $currentFrameNumber"
                        submitEnabled = true
                    }
                    remainingPins -> {
                        secondRollInfo = "Nice SPARE !! \n10 Pins in 2 Rolls!"
                        messageSubmitButton = "Save 2nd Roll and save this Frame $currentFrameNumber"
                        submitEnabled = true
                    }
                    in (remainingPins + 1)..99 -> {
                        secondRollInfo = "can't be above $remainingPins !"
                        messageSubmitButton = "Enter the 2nd Roll for Frame $currentFrameNumber \nand continue the game !"
                        submitEnabled = false
                    }
                }
            } else {
                secondRollInfo = "How many Pins you hit?"
                messageSubmitButton = "Enter the 2nd Roll for Frame $currentFrameNumber \nand continue the game !"
                submitEnabled = false
            }
            this.notifyChange()
        }
    }



    fun submitEntry(view : View){
        // after the player enters valid value between 0 and 10 then clicks button
        if (!secondRollEnabled && firstRoll.toInt() in 0..10 && firstRollEnabled){
            when(firstRoll.toInt()){
                10 -> submitEntryWhenFirstRollEqualsTen(view)
                in 0..9 -> submitEntryWhenFirstRollLessThanTen(view)
            }
        }
        /**
         * after the player has already entered & saved the 1st Roll that
         * didn't hit all the Pins, so player has to enter the 2nd Roll
         * that is between 0 and the remaining Pins
         */
        else if (secondRollEnabled && !secondRoll.isEmpty() && !firstRollEnabled){
            submitEntryWhenSecondRollEqualsOrLessThanRemaining(view)
        }
        // after the frame is added to the ArrayList
        else if(frameIsCompletedAndSaved){
            switchTeamsAndStartNewFrame(view)
        }
    }


    fun submitEntryWhenFirstRollEqualsTen(view : View){
        // disabled Roll 1 we don't need it anymore
        firstRollEnabled = false
        firstRollFinished = true

        // when the first Roll is Strike then give turn to other team
        messageSubmitButton = "Save this Frame $currentFrameNumber \nand go to next team ${if (activeTeamNumber == 1) teamTwoName else teamOneName}"
        submitEnabled = true

        // disable Roll 2 because don't need it anymore but show it with its info
        hasSecondRollRight = true
        secondRollEnabled = false
        secondRollFinished = true
        secondRollInfo = "<-- not needed"

        // calculate & show result for current score
        textIsFrameCompleted = "Completed !"
        frameCategory = "Strike"
        messageFirstBonus = "You will have a 1st Bonus !"
        messageSecondBonus = "Also ! a 2nd Bonus !"
        frameScore = "10"
        frameTitleInfo= "Frame $currentFrameNumber ------> Score: $frameScore"

        // calculate & show total score for current team
        calculateScoresForActiveTeamAndOpponentTeam()

        this.notifyChange()

        closeKeyboard(view)

        Toast.makeText(view.context,
                "Save Roll 1 \n Go to other Player",
                Toast.LENGTH_SHORT).show()

        addCompletedFrameObjectToList()


    }



    fun submitEntryWhenFirstRollLessThanTen(view : View){
        messageSubmitButton = "Enter 2nd Roll for Frame $currentFrameNumber \nand continue the game !"
        // Enable this button after player enters the right input for Roll 2
        submitEnabled = false

        // disactivate Roll 1 we don't need it anymore
        firstRollEnabled = false
        firstRollFinished = true
        firstRollInfo = when(firstRoll.toInt()) {
            in 0..3 -> "Bad... but it's Okay..."
            in 4..6 -> "bofff... acceptable"
            else -> "Good! Almost a Strike"
        }

        // show Roll 2
        hasSecondRollRight = true
        secondRollEnabled = true
        secondRollFinished = false
        secondRollInfo = "How many Pins you hit ?"

        // calculate & show result after the 1st Roll for current score
        textIsFrameCompleted = "Not completed !"
        frameCategory = ""
        frameScore = firstRoll
        frameTitleInfo= "Frame $currentFrameNumber ------> Score: $frameScore"

        // calculate & show total score for current team
        calculateScoresForActiveTeamAndOpponentTeam()
        this.notifyChange()

        closeKeyboard(view)

        // now the player enters the score for the 2nd frame
        Toast.makeText(view.context,
                "ENTER 2nd Roll",
                Toast.LENGTH_SHORT).show()
    }



    fun submitEntryWhenSecondRollEqualsOrLessThanRemaining(view : View){
        // keep Roll 2 but disable it
        hasSecondRollRight = true
        secondRollEnabled = false
        secondRollFinished = true

        // calculate & show result for current score
        textIsFrameCompleted = "Completed !"
        frameScore = (firstRoll.toInt() + secondRoll.toInt()).toString()
        frameCategory = if (frameScore.toInt() == 10)"Spare" else "Closed"
        messageFirstBonus = if (frameScore.toInt() == 10) "You will have a 1st Bonus !" else ""
        frameTitleInfo = "Frame $currentFrameNumber ------> Score: $frameScore"

        // calculate & show total score for current team
        calculateScoresForActiveTeamAndOpponentTeam()

        // after the 2nd Rolls is enter and user clicks on Button
        messageSubmitButton = "Save this Frame $currentFrameNumber \nand go to next team ${if (activeTeamNumber == 1) teamTwoName else teamOneName}"
        submitEnabled = true

        this.notifyChange()

        closeKeyboard(view)

        // now player gives turn to other Player
        Toast.makeText(view.context,
                "2nd Roll completed \n1st Roll completed \ngo to Next playerl",
                Toast.LENGTH_SHORT).show()

        addCompletedFrameObjectToList()

    }



    private fun addCompletedFrameObjectToList(){
        val frame : Frame = Frame(
                currentFrameNumber,
                frameScore.toInt(),
                frameCategory,
                textIsFrameCompleted,
                frameTitleInfo,
                firstRoll.toInt(),
                firstRollInfo,
                if (secondRoll.isEmpty()) 0 else secondRoll.toInt(),
                secondRollInfo,
                if (activeTeamNumber == 1) teamOneName else teamTwoName,
                activeTeamTotalScore.toInt()
        )
        when(activeTeamNumber){
            1 -> {
                teamOneFramesList.add(frame)
                frameIsCompletedAndSaved = true
            }
            2 -> {
                teamTwoFramesList.add(frame)
                frameIsCompletedAndSaved = true
            }
        }
    }

    private fun closeKeyboard(view : View){
        (view.context as AppCompatActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun switchTeamsAndStartNewFrame(view : View) {
        switchMenuTitle(view)
        switchActiveTeam()
        goToNextFrame()
        resetModelView()
    }

    private fun switchActiveTeam(){
        activeTeamNumber = if (activeTeamNumber == 1) 2 else 1
        opponentTeamNumber = if (opponentTeamNumber == 1) 2 else 1
    }

    private fun switchMenuTitle(view : View){
        var currentMenuTitle : String = (view.context as AppCompatActivity).title as String
        currentMenuTitle = if (currentMenuTitle.equals(teamOneName)) teamTwoName else teamOneName
        (view.context as AppCompatActivity).setTitle(currentMenuTitle)
    }

    private fun goToNextFrame(){
        currentFrameNumber++
    }

    private fun resetModelView(){

        firstRoll = ""
        firstRollInfo = ""
        firstRollEnabled = true
        firstRollFinished = false

        secondRoll = ""
        secondRollInfo = ""
        secondRollEnabled = false
        hasSecondRollRight = false
        secondRollFinished = false

        submitEnabled = false
        messageSubmitButton = "Save the 1st Roll \nand continue the game !"

        frameTitleInfo = "Frame $currentFrameNumber ------> Score: 0"
        frameScore = "0"
        frameCategory = ""
        textIsFrameCompleted = ""

        messageFirstBonus = ""
        messageSecondBonus = ""

        this.notifyChange()
    }


    private fun calculateScoresForActiveTeamAndOpponentTeam(){

        when(activeTeamNumber){
            1 -> {
                if(teamOneFramesList.size > 0){
                    var totalScore = activeTeamTotalScore.toInt()
                    for (position in 0..10){
                        totalScore += teamOneFramesList.get(position).score
                    }
                    activeTeamTotalScore = totalScore.toString()
                } else {
                    activeTeamTotalScore = frameScore
                }
                if(teamTwoFramesList.size > 0){
                    var totalScore = opponentTeamTotalScore.toInt()
                    for (position in 0..10){
                        totalScore += teamTwoFramesList.get(position).score
                    }
                    opponentTeamTotalScore = totalScore.toString()
                } else {
                    opponentTeamTotalScore = "0"
                }
            }
            2 -> {
                if(teamOneFramesList.size > 0){
                    var totalScore = opponentTeamTotalScore.toInt()
                    for (position in 0..10){
                        totalScore += teamOneFramesList.get(position).score
                    }
                    opponentTeamTotalScore = totalScore.toString()
                } else {
                    opponentTeamTotalScore = "0"
                }
                if(teamTwoFramesList.size > 0){
                    var totalScore = activeTeamTotalScore.toInt()
                    for (position in 0..10){
                        totalScore += teamTwoFramesList.get(position).score
                    }
                    activeTeamTotalScore = totalScore.toString()
                } else {
                    activeTeamTotalScore = frameScore
                }
            }
        }
    }



}
