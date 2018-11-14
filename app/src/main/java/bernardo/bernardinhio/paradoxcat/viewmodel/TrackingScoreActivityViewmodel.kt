package bernardo.bernardinhio.paradoxcat.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.graphics.Point
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import bernardo.bernardinhio.paradoxcat.Bonus
import bernardo.bernardinhio.paradoxcat.FrameCategory
import bernardo.bernardinhio.paradoxcat.Frame
import bernardo.bernardinhio.paradoxcat.R
import bernardo.bernardinhio.paradoxcat.model.TrackingScoreActivityModel
import bernardo.bernardinhio.paradoxcat.view.FragmentAnimationCongratulations
import bernardo.bernardinhio.paradoxcat.view.TrackingScoreActivityView
import java.util.*
import kotlin.collections.ArrayList

class TrackingScoreActivityViewmodel(
        val teamOneName : String, // fields passed from Launcher Activity
        val teamTwoName : String,

        var goToNextTeam : Boolean = false,
        var activeTeamNumber: Int = 1,
        var opponentTeamNumber: Int = 2,
        var countNotifyTeamOneEntersExtra : Int = 0,
        var countNotifyTeamTwoEntersExtra : Int = 0,

        var activeTeamTotalScore : String = "0",
        var opponentTeamTotalScore : String = "0",

        var firstRollScore: String = "",
        var firstRollInfo: String = "",
        var firstRollEnabled : Boolean = true,
        var firstRollFinished : Boolean = false,

        var secondRollScore: String = "",
        var secondRollInfo: String = "",
        var secondRollEnabled : Boolean = false,
        var hasSecondRollRight : Boolean = false,
        var secondRollFinished : Boolean = false,

        var submitEnabled : Boolean = false,
        var messageSubmitButton: String = "Save the 1st Roll \nand continue the game !",

        var frameNumber : Int = 1,
        var frameTitleInfo: String = "Frame 1 ------> Score: 0",
        var frameScore : String = "0",
        var frameCategory : String = FrameCategory.DEFAULT.categoryName,
        var textIsFrameCompleted : String = "",

        var messageFirstBonusReceived : String = "",
        var messageSecondBonusReceived : String = "",
        var messageFirstBonusGiven : String = "",
        var messageSecondBonusGiven : String = "",

        var isButtonReshowAnimationVisible : Boolean = false

): Observer, BaseObservable(){

    lateinit var pageFrame : Frame
    val teamOneFramesList : ArrayList<Frame> = ArrayList()
    val teamTwoFramesList : ArrayList<Frame> = ArrayList()
    var frameIsCompletedAndSaved : Boolean = false
    var teamOneNeedsExtra = false
    var teamTwoNeedsExtra = false
    var extraNumberTeamOne = 1
    var extraNumberTeamTwo = 1
    var countSwitchGameToExtraMode = 0
    var goToTeamTwoExtra : Boolean = false
    var goToTeamOneExtra : Boolean = false

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
                // use later if you need database that deals with the Model
            }
        }
    }

    fun afterTextChangedFirstRoll(editable : Editable){
        if (!secondRollEnabled ){
            // after user enters any number to 1st Roll
            if (!firstRollScore.isEmpty()){
                when(firstRollScore.toInt()){
                    in 0..9 -> {
                        firstRollInfo = "Oh not all of the Pins... \nLater you try next Roll !"
                        submitEnabled = true
                    }
                    10 -> {
                        firstRollInfo = "Perfect ! \n10 Pins in 1 Roll !"
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
            if (!secondRollScore.isEmpty()){
                val remainingPins = 10 - firstRollScore.toInt()
                when(secondRollScore.toInt()){
                    in 0..(remainingPins - 1) -> {
                        secondRollInfo = "Ah.. didn't get them all \n...Bad luck !"
                        // after the 2nd Rolls is enter
                        messageSubmitButton = "Save 2nd Roll and calculate \nscore after this Frame $frameNumber"
                        goToNextTeam = false
                        submitEnabled = true
                    }
                    remainingPins -> {
                        secondRollInfo = "Nice SPARE !! \n10 Pins in 2 Rolls!"
                        messageSubmitButton = "Save 2nd Roll and save \nthis Frame $frameNumber"
                        goToNextTeam = false
                        submitEnabled = true
                    }
                    in (remainingPins + 1)..99 -> {
                        secondRollInfo = "can't be above $remainingPins !"
                        messageSubmitButton = "Enter 2nd Roll for Frame $frameNumber \nand continue the game !"
                        goToNextTeam = false
                        submitEnabled = false
                    }
                }
            } else {
                secondRollInfo = "How many Pins you hit?"
                messageSubmitButton = "Enter the 2nd Roll for Frame $frameNumber \nand continue the game !"
                goToNextTeam = false
                submitEnabled = false
            }
            this.notifyChange()
        }
    }



    fun submitEntry(view : View){
        if (!didBothTeamsReachedTenFrames()){
            // after the player enters valid value between 0 and 10 then clicks button
            if (!secondRollEnabled && !firstRollScore.isEmpty() && firstRollScore.toInt() in 0..10 && firstRollEnabled){
                when(firstRollScore.toInt()){
                    10 -> submitEntryWhenFirstRollEqualsTen(view)
                    in 0..9 -> submitEntryWhenFirstRollLessThanTen(view)
                }
            }
            /**
             * after the player has already entered & saved the 1st Roll that
             * didn't hit all the Pins, so player has to enter the 2nd Roll
             * that is between 0 and the remaining Pins
             */
            else if (secondRollEnabled && !secondRollScore.isEmpty() && !firstRollEnabled){
                submitEntryWhenSecondRollEqualsOrLessThanRemaining(view)
            }
            // after the frame is added to the ArrayList
            else if(frameIsCompletedAndSaved){
                switchTeamsAndRestartScoring(view)
            }

        } else { // when reaching the 10th Frame

            checkIfTeamOneNeedsExtra()
            checkIfTeamTwoNeedsExtra()

            if(countSwitchGameToExtraMode == 0){
                // when bothTeams reach 10 Frames, the active team will be Team2
                if (teamOneNeedsExtra){
                    switchTeamsAndRestartScoring(view)
                    prepareUiForExtra(view)
                    messageSubmitButton ="Check if previous Frames need \nBonuses and calculate their scores"

                    notifyTeamStartedExtrasPhase(view, 1)

                } else if(teamTwoNeedsExtra){
                    prepareUiForExtra(view)
                    messageSubmitButton ="Check if previous Frames need \nBonuses and calculate their scores"

                    notifyTeamStartedExtrasPhase(view, 2)
                }
                countSwitchGameToExtraMode++
            }


            when(activeTeamNumber){
                2 -> {
                    if (teamTwoNeedsExtra){

                        if (!firstRollScore.isEmpty() && firstRollScore.toInt() in 0..10){

                            // first click to save data and check previous bonuses
                            if (!goToTeamOneExtra){

                                // after filling the Bonus for team2 and clicking submit
                                messageSubmitButton ="Check if previous Frames need \nBonuses and calculate their scores"
                                frameTitleInfo= "Extra # $extraNumberTeamTwo -------> Score ${if(firstRollScore.isEmpty()) 0 else firstRollScore}"
                                firstRollEnabled = false
                                firstRollFinished =  true
                                setExtraRollMessage()
                                notifyChange()
                                closeKeyboard(view)

                                extraNumberTeamTwo++
                                collectExtraRollAndUpdateTeamTwoBonuses(view)
                            }

                            // second click to switch to next team if it needs Bonus
                            if(teamOneNeedsExtra && goToTeamOneExtra){

                                switchTeamsAndRestartScoring(view)
                                prepareUiForExtra(view)
                                messageSubmitButton ="Please fill this extra Roll \nto complete your previous Frames"
                                frameTitleInfo= "Extra # $extraNumberTeamOne -------> Score ${if(firstRollScore.isEmpty()) 0 else firstRollScore}"
                                notifyChange()
                                closeKeyboard(view)

                                notifyTeamStartedExtrasPhase(view, 1)
                            }

                            goToTeamOneExtra = !goToTeamOneExtra

                            notifyTeamStartedExtrasPhase(view, 2)
                        }
                    }
                }
                1 -> {
                    if(teamOneNeedsExtra){

                        if (!firstRollScore.isEmpty() && firstRollScore.toInt() in 0..10){

                            // first click to save data and check previous bonuses
                            if (!goToTeamTwoExtra){

                                messageSubmitButton ="Check if previous Frames need \nBonuses and calculate their scores"
                                frameTitleInfo= "Extra # $extraNumberTeamOne -------> Score ${if(firstRollScore.isEmpty()) 0 else firstRollScore}"
                                firstRollEnabled = false
                                firstRollFinished =  true
                                setExtraRollMessage()
                                notifyChange()
                                closeKeyboard(view)

                                extraNumberTeamOne++
                                collectExtraRollAndUpdateTeamOneBonuses(view)
                            }

                            // second click to switch to next team if it needs Bonus
                            if (teamTwoNeedsExtra && goToTeamTwoExtra){

                                switchTeamsAndRestartScoring(view)
                                prepareUiForExtra(view)
                                messageSubmitButton ="Please fill this extra Roll \nto complete your previous Frames"
                                frameTitleInfo= "Extra # $extraNumberTeamTwo -------> Score ${if(firstRollScore.isEmpty()) 0 else firstRollScore}"
                                notifyChange()
                                closeKeyboard(view)

                                notifyTeamStartedExtrasPhase(view, 2)
                            }

                            goToTeamTwoExtra = !goToTeamTwoExtra

                            notifyTeamStartedExtrasPhase(view, 1)
                        }
                    } else if (teamTwoNeedsExtra){ // the final extra before end of the game
                        // switch team
                        switchTeamsAndRestartScoring(view)
                        prepareUiForExtra(view)
                        messageSubmitButton ="Please fill this extra Roll \nto complete your previous Frames"
                        frameTitleInfo= "Extra # $extraNumberTeamTwo -------> Score ${if(firstRollScore.isEmpty()) 0 else firstRollScore}"
                        notifyChange()
                        closeKeyboard(view)
                    }
                }
            }

            // show animation result
            if(!teamOneNeedsExtra && !teamTwoNeedsExtra) {

                prepareUiForExtra(view)
                firstRollEnabled = false
                notifyChange()

                disableScreenAndShowResultGameAnimation(view)
            }

        }
    }


    private fun setExtraRollMessage(){
        when(firstRollScore.toInt()){
            10 -> firstRollInfo = "Booom! 10 Pins extra... You will \ngive 10 to a previous Frame"
            in 6..9 -> firstRollInfo = "Good! many points to a Previous Frame"
            in 2..5 -> firstRollInfo = "Not Bad! some points to a Previous Frame"
            in 0..1 -> firstRollInfo = "Ohh ! You missed many Extra points"
        }
    }



    fun prepareUiForExtra(view : View){
        firstRollEnabled = true
        firstRollFinished = false
        hasSecondRollRight = false
        calculateScoresForActiveTeamAndOpponentTeam()
        notifyChange()
        closeKeyboard(view)
    }


    fun checkIfTeamOneNeedsExtra(){
        for (frame in teamOneFramesList){
            teamOneNeedsExtra = (frame.needsSecondBonus || frame.needsFirstBonus)
        }
    }

    fun checkIfTeamTwoNeedsExtra(){
        for (frame in teamTwoFramesList){
            teamTwoNeedsExtra = (frame.needsSecondBonus || frame.needsFirstBonus)
        }
    }


    fun collectExtraRollAndUpdateTeamOneBonuses(view : View){

        var frame : Frame
        for(i in 0..(teamOneFramesList.size - 1)){ // check all the Frames

            frame = teamOneFramesList.get(i)

            if (frame.needsSecondBonus){
                val points = firstRollScore.toInt()
                frame.secondBonusReceived.points = points
                frame.secondBonusReceived.providerFrame = null
                frame.secondBonusReceived.providerRollNumber = 0
                frame.secondBonusReceived.extraNumber = extraNumberTeamOne
                frame.secondBonusReceived.message = "Got $points from: Extra # $extraNumberTeamOne"
                frame.needsSecondBonus = false
                // update the score of the receiver Frame with the new second bonus value
                frame.score = frame.score + frame.secondBonusReceived.points

                messageFirstBonusGiven = "You just gave $firstRollScore points to Frame #${frame.number}"
                calculateScoresForActiveTeamAndOpponentTeam()
                notifyChange()

                break

            } else if (frame.needsFirstBonus){
                val points = firstRollScore.toInt()
                frame.firstBonusReceived.points = points
                frame.firstBonusReceived.providerFrame = null
                frame.firstBonusReceived.providerRollNumber = 0
                frame.firstBonusReceived.extraNumber = extraNumberTeamTwo
                frame.firstBonusReceived.message = "Got $points from: Extra # $extraNumberTeamTwo"
                frame.needsFirstBonus = false
                // update the score of the receiver Frame with the new first bonus value
                frame.score = frame.score + frame.firstBonusReceived.points

                messageSecondBonusGiven = "You just gave $firstRollScore points to Frame #${frame.number}"
                calculateScoresForActiveTeamAndOpponentTeam()
                notifyChange()

                break
            }
        }
    }



    fun collectExtraRollAndUpdateTeamTwoBonuses(view : View){

        var frame : Frame
        for(i in 0..(teamOneFramesList.size - 1)){ // check all the Frames

            frame = teamTwoFramesList.get(i)

            if (frame.needsSecondBonus){
                val points = firstRollScore.toInt()
                frame.secondBonusReceived.points = points
                frame.secondBonusReceived.providerFrame = null
                frame.secondBonusReceived.providerRollNumber = 0
                frame.secondBonusReceived.extraNumber = extraNumberTeamOne
                frame.secondBonusReceived.message = "Got $points from: Extra # $extraNumberTeamOne"
                frame.needsSecondBonus = false
                // update the score of the receiver Frame with the new second bonus value
                frame.score = frame.score + frame.secondBonusReceived.points

                messageFirstBonusGiven = "You just gave $firstRollScore points to Frame #${frame.number}"
                calculateScoresForActiveTeamAndOpponentTeam()
                notifyChange()

                break

            } else if (frame.needsFirstBonus){
                val points = firstRollScore.toInt()
                frame.firstBonusReceived.points = points
                frame.firstBonusReceived.providerFrame = null
                frame.firstBonusReceived.providerRollNumber = 0
                frame.firstBonusReceived.extraNumber = extraNumberTeamTwo
                frame.firstBonusReceived.message = "Got $points from: Extra # $extraNumberTeamTwo"
                frame.needsFirstBonus = false
                // update the score of the receiver Frame with the new first bonus value
                frame.score = frame.score + frame.firstBonusReceived.points

                messageSecondBonusGiven = "Just gave $firstRollScore points to Frame ${frame.number} !"
                calculateScoresForActiveTeamAndOpponentTeam()
                notifyChange()

                break
            }
        }
    }




    fun submitEntryWhenFirstRollEqualsTen(view : View){
        // disabled Roll 1 we don't need it anymore
        firstRollEnabled = false
        firstRollFinished = true

        // when the first Roll is Strike then give turn to other team
        messageSubmitButton = "Save this Frame $frameNumber \nand go to next team ${getActiveTeamName()}"
        goToNextTeam = true
        submitEnabled = true

        // disable Roll 2 because don't need it anymore but show it with its info
        hasSecondRollRight = true
        secondRollEnabled = false
        secondRollFinished = true
        secondRollInfo = "<-- not needed"

        // calculate & show result for current score
        textIsFrameCompleted = "Rolls completed"
        frameCategory = FrameCategory.STRIKE.categoryName
        messageFirstBonusReceived = "You will have a 1st Bonus !"
        messageSecondBonusReceived = "Also... a 2nd Bonus !"
        frameScore = "10"
        frameTitleInfo= "Frame $frameNumber ------> Score: $frameScore"

        // add Frame and get reference to it because it's needed to record the source of Bonus
        pageFrame = addFrameObjectToList()

        updateBonuses(pageFrame, 1)

        calculateScoresForActiveTeamAndOpponentTeam()

        closeKeyboard(view)

        notifyChange()
    }


    private fun getActiveTeamName() : String {
        return if (activeTeamNumber == 1) teamTwoName else teamOneName
    }

    fun submitEntryWhenFirstRollLessThanTen(view : View){
        messageSubmitButton = "Enter 2nd Roll for Frame $frameNumber \nand continue the game !"
        goToNextTeam = false
        submitEnabled = false

        // disactivate Roll 1 we don't need it anymore
        firstRollEnabled = false
        firstRollFinished = true
        firstRollInfo = when(firstRollScore.toInt()) {
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
        textIsFrameCompleted = "Rolls not completed"
        frameCategory = FrameCategory.DEFAULT.categoryName
        frameScore = firstRollScore
        frameTitleInfo= "Frame $frameNumber ------> Score: $frameScore"

        pageFrame = addFrameObjectToList()

        updateBonuses(pageFrame, 1)

        calculateScoresForActiveTeamAndOpponentTeam()

        closeKeyboard(view)

        notifyChange()
    }



    fun submitEntryWhenSecondRollEqualsOrLessThanRemaining(view : View){
        // keep Roll 2 but disable it
        hasSecondRollRight = true
        secondRollEnabled = false
        secondRollFinished = true

        // calculate & show result for current score
        textIsFrameCompleted = "Rolls completed"
        frameScore = (firstRollScore.toInt() + secondRollScore.toInt()).toString()
        frameCategory = if (frameScore.toInt() == 10) FrameCategory.SPARE.categoryName else FrameCategory.CLOSED.categoryName
        messageFirstBonusReceived = if (frameScore.toInt() == 10) "You will have a 1st Bonus !" else ""
        frameTitleInfo = "Frame $frameNumber ------> Score: $frameScore"

        // after the 2nd Rolls is enter and user clicks on Button
        messageSubmitButton = "Save this Frame $frameNumber \nand go to next team ${if (activeTeamNumber == 1) teamTwoName else teamOneName}"
        goToNextTeam = true
        submitEnabled = true

        updatePageFrame(pageFrame)

        updateBonuses(pageFrame, 2)

        calculateScoresForActiveTeamAndOpponentTeam()

        closeKeyboard(view)

        notifyChange()
    }

    private fun addFrameObjectToList() : Frame{
        var needsFirstBonus : Boolean = false
        var needsSecondBonus : Boolean = false
        when(frameCategory){
            FrameCategory.STRIKE.categoryName -> {
                needsFirstBonus = true
                needsSecondBonus = true
            }
            else -> {
                needsFirstBonus = false
                needsSecondBonus = false
            }
        }
        val frame : Frame = Frame(
                frameNumber,
                frameScore.toInt(),
                frameCategory,
                textIsFrameCompleted,
                frameTitleInfo,
                firstRollScore.toInt(),
                firstRollInfo,
                if (secondRollScore.isEmpty()) 0 else secondRollScore.toInt(),
                secondRollInfo,
                if (activeTeamNumber == 1) teamOneName else teamTwoName,
                activeTeamTotalScore.toInt(),
                needsFirstBonus,
                needsSecondBonus
        ) // we add Bonus later on previous Frames already saved
        when(activeTeamNumber){
            1 -> {
                if (teamOneFramesList.size <= 10){
                    teamOneFramesList.add(frame)
                    frameIsCompletedAndSaved = true
                }
            }
            2 -> {
                if (teamTwoFramesList.size <= 10){
                    teamTwoFramesList.add(frame)
                    frameIsCompletedAndSaved = true
                }
            }
        }
        return frame
    }

    private fun updatePageFrame(pageFrame : Frame){
        var needsFirstBonus : Boolean = false
        var needsSecondBonus : Boolean = false
        when(frameCategory){
            FrameCategory.SPARE.categoryName -> needsFirstBonus = true
            else -> {
                needsFirstBonus = false
                needsSecondBonus = false
            }
        }
        pageFrame.score = frameScore.toInt()
        pageFrame.category = frameCategory
        pageFrame.textIsFrameCompleted = textIsFrameCompleted
        pageFrame.titleInfo = frameTitleInfo
        pageFrame.secondRollScore = secondRollScore.toInt()
        pageFrame.secondRollInfo = secondRollInfo
        pageFrame.teamName = if (activeTeamNumber == 1) teamOneName else teamTwoName
        pageFrame.teamTotalScore = activeTeamTotalScore.toInt()
        pageFrame.needsFirstBonus = needsFirstBonus
        pageFrame.needsSecondBonus = needsSecondBonus
    }

    private fun closeKeyboard(view : View){
        (view.context as AppCompatActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun switchTeamsAndRestartScoring(view : View) {
        switchMenuTitle(view)
        switchActiveTeam()
        goToNextFrameIfBothTeamsPlayedSameFrameNumber()
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

    private fun goToNextFrameIfBothTeamsPlayedSameFrameNumber(){
        if (teamOneFramesList.size == frameNumber && teamTwoFramesList.size == frameNumber){
            frameNumber++
        }
    }

    private fun didBothTeamsReachedTenFrames() : Boolean{
        return (teamOneFramesList.size == 5 && teamTwoFramesList.size == 5)
    }


    private fun resetModelView(){
        firstRollScore = ""
        firstRollInfo = ""
        firstRollEnabled = true
        firstRollFinished = false

        secondRollScore = ""
        secondRollInfo = ""
        secondRollEnabled = false
        hasSecondRollRight = false
        secondRollFinished = false

        messageSubmitButton = "Save the 1st Roll \nand continue the game !"
        goToNextTeam =  false
        submitEnabled = false

        frameTitleInfo = "Frame $frameNumber ------> Score: 0"
        frameScore = "0"
        frameCategory = FrameCategory.DEFAULT.categoryName
        textIsFrameCompleted = ""

        messageFirstBonusReceived = ""
        messageSecondBonusReceived = ""
        messageFirstBonusGiven = ""
        messageSecondBonusGiven = ""

        // update the UI by switching current player and opponent_total_score player score
        calculateScoresForActiveTeamAndOpponentTeam()

        notifyChange()
    }





    private fun calculateScoresForActiveTeamAndOpponentTeam(){
        // by default for team1 we use the ArrayList1 and for team2 we use the ArrayList2
        when(activeTeamNumber){
            // case activeTeamTotalScore is in ArrayList1 and opponentTeamTotalScore in ArrayList2
            1 -> {
                // calculate the activeTeamTotalScore
                if(teamOneFramesList.size > 0){
                    activeTeamTotalScore = getTotalScoreTeamOneFramesList().toString()
                } else {
                    // when it's the first frame and team1 didn't add the Frame to the ArrayList yet because not all of its values are ready except the frame score which we have and we want to display
                    activeTeamTotalScore = frameScore
                }
                // calculate the opponentTeamTotalScore
                if(teamTwoFramesList.size > 0){
                    opponentTeamTotalScore = getTotalScoreTeamTwoFramesList().toString()
                } else {
                    // when it's the first frame and the opponent_total_score didn't play yet
                    opponentTeamTotalScore = "0"
                }
            }
            // case activeTeamTotalScore is in ArrayList2 and opponentTeamTotalScore in ArrayList1
            2 -> {
                // calculate the activeTeamTotalScore
                if(teamTwoFramesList.size > 0){
                    activeTeamTotalScore = getTotalScoreTeamTwoFramesList().toString()
                } else {
                    // when it's the first frame and team1 didn't add the Frame to the ArrayList yet because not all of its values are ready except the frame score which we have and we want to display
                    activeTeamTotalScore = frameScore
                }
                // calculate the opponentTeamTotalScore
                if(teamOneFramesList.size > 0){
                    opponentTeamTotalScore = getTotalScoreTeamOneFramesList().toString()
                } else {
                    opponentTeamTotalScore = "0"
                }
            }
        }
        notifyChange()
    }

    private fun updateBonuses(providerFrame : Frame, providerRollNumber : Int){
        when(activeTeamNumber){
            1 -> { // activeTeam is team1 and team1 always uses ArrayList1
                updateBonuses(teamOneFramesList, providerFrame, providerRollNumber)
            }
            2 -> { // activeTeam is team1 and team1 always uses ArrayList1
                updateBonuses(teamTwoFramesList, providerFrame, providerRollNumber)
            }
        }
    }


    private fun updateBonuses(teamFramesList : ArrayList<Frame>, providerFrame : Frame, providerRollNumber : Int){
        val points = calculateProvidedPoints(providerFrame, providerRollNumber)
        if(teamFramesList.size > 0 && points != 0){
            // the current Frame is the provider
            val currentPositionInList = teamFramesList.indexOf(providerFrame)

            // the 2nd Frame of index 1 in the list can update only the first Frame of index 0 // the 2nd Frame isn't caught by the Kotlin Loop from 0 to 0
            if (currentPositionInList == 1){ // this means the 2nd Frame
                val receiverFrame : Frame = teamFramesList.get(0)
                // start always by the 2nd Roll that needs to be updated in the case of Strike
                if (receiverFrame.needsSecondBonus){
                    updateReceiverFrameSecondBonusReceived(receiverFrame, points, providerFrame, providerRollNumber)
                    // update the Provider given Bonuses
                    when(providerRollNumber){
                        1 -> updateProviderFrameFirstBonusGiven(providerFrame, receiverFrame.number, receiverFrame.secondBonusReceived)
                        2 -> updateProviderFrameSecondBonusGiven(providerFrame, receiverFrame.number, receiverFrame.secondBonusReceived)
                    }
                } else if (receiverFrame.needsFirstBonus){ // this case of Strike OR Spare
                    updateReceiverFrameFirstBonusReceived(receiverFrame, points, providerFrame, providerRollNumber)
                    when(providerRollNumber){
                        1 -> updateProviderFrameFirstBonusGiven(providerFrame, receiverFrame.number, receiverFrame.firstBonusReceived)
                        2 -> updateProviderFrameSecondBonusGiven(providerFrame, receiverFrame.number, receiverFrame.firstBonusReceived)
                    }
                }
            }

            else if(currentPositionInList != 0 && currentPositionInList != 1){
                // The first Frame located at 0 doesn't update any previous Frames
                for (index in 0 until currentPositionInList - 1 step 1){
                    val receiverFrame : Frame = teamFramesList.get(index)
                    if (receiverFrame.needsSecondBonus){
                        updateReceiverFrameSecondBonusReceived(receiverFrame, points, providerFrame, providerRollNumber)
                        when(providerRollNumber){
                            1 -> updateProviderFrameFirstBonusGiven(providerFrame, receiverFrame.number, receiverFrame.secondBonusReceived)
                            2 -> updateProviderFrameSecondBonusGiven(providerFrame, receiverFrame.number, receiverFrame.secondBonusReceived)
                        }
                        break
                    } else if (receiverFrame.needsFirstBonus){ // this case of Strike Or Spare
                        updateReceiverFrameFirstBonusReceived(receiverFrame, points, providerFrame, providerRollNumber)
                        when(providerRollNumber){
                            1 -> updateProviderFrameFirstBonusGiven(providerFrame, receiverFrame.number, receiverFrame.firstBonusReceived)
                            2 -> updateProviderFrameSecondBonusGiven(providerFrame, receiverFrame.number, receiverFrame.firstBonusReceived)
                        }
                        break
                    }
                }
            }

        }
    }

    private fun updateProviderFrameFirstBonusGiven(providerFrame : Frame, receiverFrameNumber : Int, receiverFrameBonusReceived : Bonus){
        providerFrame.firstBonusGiven = receiverFrameBonusReceived
        providerFrame.firstBonusGiven.message = "You gave ${providerFrame.firstBonusGiven.points} to: Frame: $receiverFrameNumber"
        messageFirstBonusGiven = providerFrame.firstBonusGiven.message
    }

    private fun updateProviderFrameSecondBonusGiven(providerFrame : Frame, receiverFrameNumber : Int, receiverFrameBonusReceived : Bonus){
        providerFrame.secondBonusGiven = receiverFrameBonusReceived
        providerFrame.secondBonusGiven.message = "You gave ${providerFrame.secondBonusGiven.points} to: Frame: $receiverFrameNumber"
        messageSecondBonusGiven = providerFrame.secondBonusGiven.message
    }


    private fun calculateProvidedPoints(providerFrame : Frame, providerRollNumber : Int) : Int{
        return if(providerRollNumber == 1) providerFrame.firstRollScore else providerFrame.secondRollScore
    }

    private fun updateReceiverFrameSecondBonusReceived(receiverFrame : Frame, points : Int, providerFrame : Frame, providerRollNumber : Int){
        receiverFrame.secondBonusReceived.points = points
        receiverFrame.secondBonusReceived.providerFrame = providerFrame
        receiverFrame.secondBonusReceived.providerRollNumber = providerRollNumber
        receiverFrame.secondBonusReceived.message = "Got $points from: Frame: ${providerFrame.number} Roll: $providerRollNumber"
        receiverFrame.needsSecondBonus = false
        // update the score of the receiver Frame with the new second bonus value
        receiverFrame.score = receiverFrame.score + receiverFrame.secondBonusReceived.points
        Log.d("previousFrames", "Frame #${receiverFrame.number} is is ${receiverFrame.category} and took $points as 2nd Bonus")
        Log.d("previousFrames", "Frame #${receiverFrame.number} updated score ${receiverFrame.score}")
    }

    private fun updateReceiverFrameFirstBonusReceived(receiverFrame : Frame, points : Int, providerFrame : Frame, providerRollNumber : Int){
        receiverFrame.firstBonusReceived.points = points
        receiverFrame.firstBonusReceived.providerFrame = providerFrame
        receiverFrame.firstBonusReceived.providerRollNumber = providerRollNumber
        receiverFrame.firstBonusReceived.message = "Got $points from: Frame: ${providerFrame.number} Roll: $providerRollNumber"
        receiverFrame.needsFirstBonus = false
        // update the score of the receiver Frame with the new first bonus value
        receiverFrame.score = receiverFrame.score + receiverFrame.firstBonusReceived.points
        Log.d("previousFrames", "Frame #${receiverFrame.number} is ${receiverFrame.category} and took $points as 1st Bonus")
        Log.d("previousFrames", "Frame #${receiverFrame.number} updated score ${receiverFrame.score}")
    }

    private fun getTotalScoreTeamOneFramesList() : Int {
        var totalScore = 0
        for (frame in teamOneFramesList){
            totalScore += frame.score
        }
        return totalScore
    }

    private fun getTotalScoreTeamTwoFramesList() : Int {
        var totalScore = 0
        for (frame in teamTwoFramesList){
            totalScore += frame.score
        }
        return totalScore
    }


    private fun disableScreenAndShowResultGameAnimation(view : View){
        submitEnabled = false
        notifyChange()
        inflateFragmentAnimation(view)
    }

    fun showAnimationFromActivity(context : Context){
        inflateFragmentAnimation(View(context))
    }

    fun showAnimationFromButton(view : View){
        inflateFragmentAnimation(view)
    }

    private fun inflateFragmentAnimation(view : View){
        // In case we want the animation to appear from menu for example before the game finishes
        submitEnabled = false
        messageSubmitButton ="SHOW RESULTS \n ${getWinnerTeamName()} won?! see details..."
        frameTitleInfo= "Game ends now..."
        isButtonReshowAnimationVisible = true
        notifyChange()

        // get the variables needed for the Animation
        val trackingScoreActivityView : TrackingScoreActivityView = view.context as TrackingScoreActivityView
        var containerForFragment: FrameLayout? = null
        var fragmentManager: FragmentManager? = null
        var fragmentTransaction: FragmentTransaction? = null
        var fragment: FragmentAnimationCongratulations? = null
        var animationForFragment: Animation? = null

        // inflate the fragment containing the Animation
        containerForFragment = trackingScoreActivityView.findViewById<View>(R.id.container_fragment_animation_next_article) as FrameLayout
        fragmentManager = trackingScoreActivityView.supportFragmentManager
        fragmentTransaction = fragmentManager!!.beginTransaction()
        fragment = FragmentAnimationCongratulations()
        fragmentTransaction!!.replace(R.id.container_fragment_animation_next_article, fragment)
        fragmentTransaction!!.commitNow() // synchronized

        // bring to front of the Adds
        containerForFragment!!.visibility = View.VISIBLE
        containerForFragment!!.bringToFront()

        animationForFragment = AnimationUtils.loadAnimation(
                trackingScoreActivityView.applicationContext,
                R.anim.anim_congratulations_bottom)

        // configure animation programmatically then set the Animation Listener
        animationForFragment!!.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {

                // calculate the height of the screen
                val display = trackingScoreActivityView.windowManager.defaultDisplay
                val outSize = Point()
                display.getSize(outSize)
                val heightScreen = outSize.y

                // calculate the offset position of the notification layout when it is created after the menu
                val outLocation = IntArray(2)
                containerForFragment!!.getLocationOnScreen(outLocation)
                val windowsTop = outLocation[1]

                // set Y position at the top of screen
                containerForFragment!!.translationY = 0f

                // setup the Fragment values
                fragment!!.tvCongratulationsMessage!!.text = "It seems someone has \nwon my game! \n\nCongratulations \n${getWinnerTeamName()} \n\nSCORE ${getWinnerTeamScore()}"
                fragment!!.tvResultsTeamOne!!.text = "$teamOneName's score: ${getTotalScoreTeamOneFramesList()}"
                fragment!!.tvResultsTeamTwo!!.text = "$teamTwoName's score: ${getTotalScoreTeamTwoFramesList()}"

                // animation onClick // close the Actvity
                containerForFragment!!.setOnClickListener {
                    trackingScoreActivityView.finish()
                }
            }
            override fun onAnimationEnd(animation: Animation) {
                containerForFragment!!.visibility = View.GONE
                fragmentTransaction!!.detach(fragment!!)
                fragment = null
            }
            override fun onAnimationRepeat(animation: Animation) {
                containerForFragment!!.visibility = View.GONE
                fragmentTransaction!!.detach(fragment!!)
                fragment = null
            }
        })

        containerForFragment!!.startAnimation(animationForFragment)
    }


    private fun getWinnerTeamScore() : Int{
        return if (getTotalScoreTeamOneFramesList() > getTotalScoreTeamTwoFramesList()) getTotalScoreTeamOneFramesList() else getTotalScoreTeamTwoFramesList()
    }

    private fun getWinnerTeamName() : String {
        var winner = ""
        if (getTotalScoreTeamOneFramesList() == getTotalScoreTeamTwoFramesList()){
            winner = "BOTH TEAM"
        } else if (getTotalScoreTeamOneFramesList() > getTotalScoreTeamTwoFramesList()) {
            winner = teamOneName
        } else winner = teamTwoName
        return winner
    }


    fun notifyTeamStartedExtrasPhase(view: View, teamNumber : Int) {
        when(teamNumber){
            1 -> {
                if(countNotifyTeamOneEntersExtra == 0) {
                    Snackbar.make(
                            view,
                            "$teamOneName, you entered Extra Rolls Phase !",
                            Snackbar.LENGTH_LONG).show()
                    countNotifyTeamOneEntersExtra++
                }
            }
            2 -> {
                if(countNotifyTeamTwoEntersExtra == 0) {
                    Snackbar.make(
                            view,
                            "$teamTwoName, you entered Extra Rolls Phase !",
                            Snackbar.LENGTH_LONG).show()
                    countNotifyTeamTwoEntersExtra++
                }
            }
        }

    }


}