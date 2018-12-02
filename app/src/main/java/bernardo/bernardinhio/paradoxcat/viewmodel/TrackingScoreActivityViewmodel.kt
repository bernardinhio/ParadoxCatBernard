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

        var uiCurrentTeamTotalScore : String = "0",
        var uiOpponentTeamTotalScore : String = "0",

        var uiFirstRollScore: String = "",
        var uiFirstRollInfo: String = "",
        var uiFirstRollEnabled : Boolean = true,
        var uiFirstRollFinished : Boolean = false,

        var uiSecondRollScore: String = "",
        var uiSecondRollInfo: String = "",
        var uiSecondRollEnabled : Boolean = false,
        var uiHasSecondRollRight : Boolean = false,
        var uiSecondRollFinished : Boolean = false,

        var uiSubmitEnabled : Boolean = false,
        var uiGoToNextTeam : Boolean = false,
        var uiTeamOneNeedsExtra : Boolean = false,
        var uiTeamTwoNeedsExtra : Boolean = false,
        var uiFrameNumber : Int = 1,
        var uiFrameScore : String = "0",
        var uiMessageSubmitButton: String = "Frame # $uiFrameNumber: Save the 1st Roll \nand continue the game !",

        var uiFrameTitleInfo: String = "Frame # $uiFrameNumber ------> Score: $uiFrameScore",
        var uiFrameCategory : String = FrameCategory.DEFAULT.categoryName,
        var uiFrameCompleted : String = "",

        // FirstBonusReceived
        var isBonusOneReceived : Boolean = false,  // use later when i develop the show past Frames feature
        var uiMessageFirstBonusReceived : String = "",
        // use later when the feature of showing each frame with its received bonuses is ready
        var uiFirstBonusReceivedPoints : String = "",
        var uiFirstBonusReceivedProviderRoll : String = "",
        var uiFirstBonusReceivedProviderFrameOrExtra : String = "",

        // SecondBonusReceived
        var isBonusTwoReceived : Boolean = false,  // use later when i develop the show past Frames feature
        var uiMessageSecondBonusReceived : String = "",
        // use later when the feature of showing each frame with its received bonuses is ready
        var uiSecondBonusReceivedPoints : String = "",
        var uiSecondBonusReceivedProviderRoll : String = "",
        var uiSecondBonusReceivedProviderFrameOrExtra : String = "",

        // FirstBonusGiven
        var uiFirstBonusGivenProviderRollOrExtra : String = "",
        var uiFirstBonusGivenPoints : String = "",
        var uiFirstBonusGivenFrameReceiver : String = "",
        var uiFirstBonusGivenBonusReceiver : String = "", // also used to change background

        // SecondBonusGiven
        var uiSecondBonusGivenProviderRollOrExtra : String = "",
        var uiSecondBonusGivenPoints : String = "",
        var uiSecondBonusGivenFrameReceiver : String = "",
        var uiSecondBonusGivenBonusReceiver : String = "", // also used to change background

        var uiIsButtonReshowAnimationVisible : Boolean = false

): Observer, BaseObservable(){

    lateinit var pageFrame : Frame
    val teamOneFramesList : ArrayList<Frame> = ArrayList()
    val teamTwoFramesList : ArrayList<Frame> = ArrayList()

    var activeTeamNumber: Int = 1
    var opponentTeamNumber: Int = 2
    var countNotifyTeamOneEntersExtra : Int = 0
    var countNotifyTeamTwoEntersExtra : Int = 0

    var frameIsCompletedAndSaved : Boolean = false

    var countTeamOneExtra = 1
    var countTeamTwoExtra = 1
    var countSwitchGameToExtraMode = 0
    var goToOtherTeamExtra : Boolean = false
    var extraRollsPhaseStarted = false

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
            if (p1 is String && p1.equals("uiFrameScore")){
                // use later if you need database that deals with the Model
            }
        }
    }

    fun afterTextChangedFirstRoll(editable : Editable){
        if (!uiSecondRollEnabled ){
            // after user enters any number to 1st Roll
            if (!uiFirstRollScore.isEmpty()){
                when(uiFirstRollScore.toInt()){
                    in 0..9 -> {
                        uiFirstRollInfo = "Oh not all of the Pins... \nLater you try next Roll !"
                        uiSubmitEnabled = true
                    }
                    10 -> {
                        uiFirstRollInfo = "Perfect ! \n10 Pins in 1 Roll !"
                        uiSubmitEnabled = true
                    }
                    in 11..99 -> { // more than 2 digits not allowed in UI
                        uiFirstRollInfo = "can't be above 10 !"
                        uiSubmitEnabled = false
                    }
                }
            } else {
                uiFirstRollInfo = "How many Pins you hit ?"
                uiSubmitEnabled = false
            }
            this.notifyChange()
        }
    }

    fun afterTextChangedSecondRoll(editable : Editable){
        // after the first roll is finished
        if (!uiFirstRollEnabled && uiFirstRollFinished){
            // first time Roll 2 is used
            if (!uiSecondRollScore.isEmpty()){
                val remainingPins = 10 - uiFirstRollScore.toInt()
                when(uiSecondRollScore.toInt()){
                    in 0..(remainingPins - 1) -> {
                        uiSecondRollInfo = "Ah.. didn't get them all \n...Bad luck !"
                        // after the 2nd Rolls is enter
                        uiMessageSubmitButton = "Save 2nd Roll and calculate \nscore after this Frame $uiFrameNumber"
                        uiGoToNextTeam = false
                        uiSubmitEnabled = true
                    }
                    remainingPins -> {
                        uiSecondRollInfo = "Nice SPARE !! \n10 Pins in 2 Rolls!"
                        uiMessageSubmitButton = "Save 2nd Roll and save \nthis Frame $uiFrameNumber"
                        uiGoToNextTeam = false
                        uiSubmitEnabled = true
                    }
                    in (remainingPins + 1)..99 -> {
                        uiSecondRollInfo = "can't be above $remainingPins !"
                        uiMessageSubmitButton = "Enter 2nd Roll for Frame $uiFrameNumber \nand continue the game !"
                        uiGoToNextTeam = false
                        uiSubmitEnabled = false
                    }
                }
            } else {
                uiSecondRollInfo = "How many Pins you hit?"
                uiMessageSubmitButton = "Enter the 2nd Roll for Frame $uiFrameNumber \nand continue the game !"
                uiGoToNextTeam = false
                uiSubmitEnabled = false
            }
            this.notifyChange()
        }
    }

    fun submitEntry(view : View){
        // after the player enters valid value between 0 and 10 then clicks button
        if (!extraRollsPhaseStarted && !uiSecondRollEnabled && !uiFirstRollScore.isEmpty() && uiFirstRollScore.toInt() in 0..10 && uiFirstRollEnabled){
            when(uiFirstRollScore.toInt()){
                10 -> submitEntryWhenFirstRollEqualsTen(view)
                in 0..9 -> submitEntryWhenFirstRollLessThanTen(view)
            }
        }
        /**
         * after the player has already entered & saved the 1st Roll that
         * didn't hit all the Pins, so player has to enter the 2nd Roll
         * that is between 0 and the remaining Pins
         */
        else if (uiSecondRollEnabled && !uiSecondRollScore.isEmpty() && !uiFirstRollEnabled){
            submitEntryWhenSecondRollEqualsOrLessThanRemaining(view)
        }
        // after the frame is added to the ArrayList
        else if(frameIsCompletedAndSaved && !didBothTeamsReachedTenFrames()){
            switchTeamsAndRestartScoring(view)
        } else if (didBothTeamsReachedTenFrames()){ // when reaching the 10th Frame
            startExtraRollsPhase(view)
            extraRollsPhaseStarted = true
        }
    }

    private fun startExtraRollsPhase(view : View){
        checkIfTeamOneNeedsExtra()
        checkIfTeamTwoNeedsExtra()

        if(countSwitchGameToExtraMode == 0){
            switchToExtraMode(view)
        }

        when(activeTeamNumber){
            // Team 1 always starts the game so team2 will be the first to switch
            2 -> addExtraWhenCurrentTeamIsTeamTwo(view)

            1 -> addExtraWhenCurrentTeamIsTeamOne(view)
        }

        // show animation result
        if(!uiTeamOneNeedsExtra && !uiTeamTwoNeedsExtra) {
            prepareUiForExtra(view)
            uiFirstRollEnabled = false
            uiFirstRollFinished = false
            notifyChange()
            disableScreenAndShowResultGameAnimation(view)
        }
    }

    private fun switchToExtraMode(view : View){
        // when bothTeams reach 10 Frames, the active team will be Team2
        if (uiTeamOneNeedsExtra){
            switchTeamsAndRestartScoring(view)
            prepareUiForExtra(view)
            uiMessageSubmitButton = "Check previous Frames \nBonuses & review all scores"
            uiFrameTitleInfo= "Extra # $countTeamTwoExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
            notifyTeamStartedExtrasPhase(view, 1)

        } else if(uiTeamTwoNeedsExtra){
            resetModelView()
            prepareUiForExtra(view)
            uiMessageSubmitButton = "Check previous Frames \nBonuses & review all scores"
            uiFrameTitleInfo= "Extra # $countTeamTwoExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
            notifyTeamStartedExtrasPhase(view, 2)
        }
        countSwitchGameToExtraMode++
        closeKeyboard(view)
    }

    private fun addExtraWhenCurrentTeamIsTeamTwo(view : View){
        if (uiTeamTwoNeedsExtra){

            if (!uiFirstRollScore.isEmpty() && uiFirstRollScore.toInt() in 0..10){

                // first click to save data and check previous bonuses
                if (!goToOtherTeamExtra){
                    // after filling the Bonus for team2 and clicking submit
                    firstClickInputScoreForTeamTwo(view)
                }
                // second click to switch to next team if it needs Bonus
                if(uiTeamOneNeedsExtra && goToOtherTeamExtra){
                    secondClickSwitchToTeamOneExtra(view)

                } else if (uiTeamTwoNeedsExtra && goToOtherTeamExtra){
                    // stay in Team2 if team2 needs Bonus and team1 doesn't need
                    secondClickStayInTeamTwoExtra(view)
                }

                goToOtherTeamExtra = !goToOtherTeamExtra
                notifyTeamStartedExtrasPhase(view, 2)
            }
        }
    }

    private fun addExtraWhenCurrentTeamIsTeamOne(view : View){
        if(uiTeamOneNeedsExtra){

            if (!uiFirstRollScore.isEmpty() && uiFirstRollScore.toInt() in 0..10){

                // first click to save data and check previous bonuses
                if (!goToOtherTeamExtra){
                    firstClickInputScoreForTeamOne(view)
                }
                // second click to switch to next team if it needs Bonus
                if (uiTeamTwoNeedsExtra && goToOtherTeamExtra){
                    secondClickSwitchToTeamTwoExtra(view)
                } else if(uiTeamOneNeedsExtra && goToOtherTeamExtra){
                    // stay in Team1 if Team one still needs Bonuses and Team2 doesn't need
                    secondClickStayInTeamOneExtra(view)
                }

                goToOtherTeamExtra = !goToOtherTeamExtra
                notifyTeamStartedExtrasPhase(view, 1)
            }
        } else if (uiTeamTwoNeedsExtra){
            // the final extra before end of the game
            secondClickSwitchToTeamTwoExtra(view)
            goToOtherTeamExtra = !goToOtherTeamExtra
        }
    }

    private fun firstClickInputScoreForTeamTwo(view : View){
        // after filling the Bonus for team2 and clicking submit
        uiMessageSubmitButton = "Save this extra # $countTeamTwoExtra \nand continue"
        uiFrameTitleInfo= "Extra # $countTeamTwoExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
        uiFirstRollEnabled = false
        uiFirstRollFinished =  true
        setExtraRollMessage()
        notifyChange()
        closeKeyboard(view)
        collectExtraRollAndUpdateTeamTwoBonuses(view)
        countTeamTwoExtra++
    }

    private fun secondClickSwitchToTeamOneExtra(view : View){
        // second click to switch to next team if it needs Bonus
        switchTeamsAndRestartScoring(view)
        prepareUiForExtra(view)
        uiMessageSubmitButton = "Fill Extra #$countTeamOneExtra to complete \nyour previous Frames"
        uiFrameTitleInfo= "Extra # $countTeamOneExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
        notifyChange()
        closeKeyboard(view)
        notifyTeamStartedExtrasPhase(view, 1)
    }

    private fun secondClickStayInTeamTwoExtra(view: View){
        // stay in Team2 if team2 needs Bonus and team1 doesn't need
        prepareUiForExtra(view)
        uiFirstRollScore = ""
        uiFirstRollInfo = "How many Pins you hit ?"
        uiMessageSubmitButton = "Fill extra #$countTeamTwoExtra to complete \nyour previous Frames"
        uiFrameTitleInfo= "Extra # $countTeamTwoExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
        resetUiFirstBonusGiven()
        notifyChange()
        closeKeyboard(view)
        notifyTeamStartedExtrasPhase(view, 2)
    }

    private fun firstClickInputScoreForTeamOne(view : View){
        // first click to save data and check previous bonuses
        uiMessageSubmitButton = "Save this Extra # $countTeamOneExtra \nand continue"
        uiFrameTitleInfo= "Extra # $countTeamOneExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
        uiFirstRollEnabled = false
        uiFirstRollFinished =  true
        setExtraRollMessage()
        notifyChange()
        closeKeyboard(view)
        collectExtraRollAndUpdateTeamOneBonuses(view)
        countTeamOneExtra++
    }

    private fun secondClickSwitchToTeamTwoExtra(view : View){
        // second click to switch to next team if it needs Bonus
        switchTeamsAndRestartScoring(view)
        prepareUiForExtra(view)
        uiMessageSubmitButton = "Fill extra #$countTeamTwoExtra to complete \nyour previous Frames"
        uiFrameTitleInfo= "Extra # $countTeamTwoExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
        notifyChange()
        closeKeyboard(view)
        notifyTeamStartedExtrasPhase(view, 2)
    }

    private fun secondClickStayInTeamOneExtra(view : View){
        // stay in Team1 if Team one still needs Bonuses and Team2 doesn't need
        prepareUiForExtra(view)
        uiFirstRollScore = ""
        uiFirstRollInfo = "How many Pins you hit ?"
        uiMessageSubmitButton = "Fill extra #$countTeamOneExtra to complete \nyour previous Frames"
        uiFrameTitleInfo= "Extra # $countTeamOneExtra -------> Score ${if(uiFirstRollScore.isEmpty()) 0 else uiFirstRollScore}"
        resetUiFirstBonusGiven()
        notifyChange()
        closeKeyboard(view)
        notifyTeamStartedExtrasPhase(view, 1)
    }

    private fun setExtraRollMessage(){
        when(uiFirstRollScore.toInt()){
            10 -> uiFirstRollInfo = "Booom! 10 points bonus to a previous Frame"
            in 6..9 -> uiFirstRollInfo = "Good! many points to a Previous Frame"
            in 2..5 -> uiFirstRollInfo = "Not Bad! some points to a Previous Frame"
            in 0..1 -> uiFirstRollInfo = "Ohh ! You missed many Extra points"
        }
    }

    fun prepareUiForExtra(view : View){
        uiFirstRollEnabled = true
        uiFirstRollFinished = false
        uiHasSecondRollRight = false
        calculateScoresForActiveTeamAndOpponentTeam()
        notifyChange()
        closeKeyboard(view)
    }

    fun checkIfTeamOneNeedsExtra(){
        for (frame in teamOneFramesList){
            if (frame.needsSecondBonus || frame.needsFirstBonus){
                uiTeamOneNeedsExtra = true
                break
            } else uiTeamOneNeedsExtra = false
        }
    }

    fun checkIfTeamTwoNeedsExtra(){
        for (frame in teamTwoFramesList){
            if (frame.needsSecondBonus || frame.needsFirstBonus){
                uiTeamTwoNeedsExtra = true
                break
            } else uiTeamTwoNeedsExtra = false
        }
    }

    fun collectExtraRollAndUpdateTeamOneBonuses(view : View){
        var frame : Frame
        for(i in 0..(teamOneFramesList.size - 1)){ // check all the Frames

            frame = teamOneFramesList.get(i)

            if (frame.needsSecondBonus){
                feedReceiverFrameSecondBonusForTeamOne(frame)
                break
            } else if (frame.needsFirstBonus){
                feedReceiverFrameFirstBonusForTeamOne(frame)
                break
            }
        }
    }

    private fun feedReceiverFrameSecondBonusForTeamOne(frame : Frame){
        val points = uiFirstRollScore.toInt()
        frame.secondBonusReceived.points = points
        frame.secondBonusReceived.providerFrame = null
        frame.secondBonusReceived.providerRollNumber = 0 // it is the extra
        frame.secondBonusReceived.extraNumber = countTeamOneExtra
        frame.secondBonusReceived.message = "Got $points Points from: Extra # $countTeamOneExtra"
        frame.needsSecondBonus = false
        // update the score of the receiver Frame with the new second bonus value
        frame.score = frame.score + frame.secondBonusReceived.points

        updateUiFirstBonusGiven(
                "Extra #$countTeamOneExtra",
                uiFirstRollScore,
                "Frame #${frame.number}",
                "B2")

        calculateScoresForActiveTeamAndOpponentTeam()
        notifyChange()
    }

    private fun feedReceiverFrameFirstBonusForTeamOne(frame : Frame){
        val points = uiFirstRollScore.toInt()
        frame.firstBonusReceived.points = points
        frame.firstBonusReceived.providerFrame = null
        frame.firstBonusReceived.providerRollNumber = 0  // it is the extra
        frame.firstBonusReceived.extraNumber = countTeamOneExtra
        frame.firstBonusReceived.message = "Got $points Points from: Extra # $countTeamTwoExtra"
        frame.needsFirstBonus = false
        // update the score of the receiver Frame with the new first bonus value
        frame.score = frame.score + frame.firstBonusReceived.points

        updateUiFirstBonusGiven(
                "Extra #$countTeamOneExtra",
                uiFirstRollScore,
                "Frame #${frame.number}",
                "B1")

        calculateScoresForActiveTeamAndOpponentTeam()
        notifyChange()
    }

    fun collectExtraRollAndUpdateTeamTwoBonuses(view : View){
        var frame : Frame
        for(i in 0..(teamOneFramesList.size - 1)){ // check all the Frames

            frame = teamTwoFramesList.get(i)

            if (frame.needsSecondBonus){
                feedReceiverFrameSecondBonusForTeamTwo(frame)
                break
            } else if (frame.needsFirstBonus){
                feedReceiverFrameFirstBonusForTeamTwo(frame)
                break
            }
        }
    }

    private fun feedReceiverFrameSecondBonusForTeamTwo(frame : Frame){
        val points = uiFirstRollScore.toInt()
        frame.secondBonusReceived.points = points
        frame.secondBonusReceived.providerFrame = null
        frame.secondBonusReceived.providerRollNumber = 0 // it is the extra
        frame.secondBonusReceived.extraNumber = countTeamTwoExtra
        frame.secondBonusReceived.message = "Got $points Points from: Extra # $countTeamOneExtra"
        frame.needsSecondBonus = false
        // update the score of the receiver Frame with the new second bonus value
        frame.score = frame.score + frame.secondBonusReceived.points

        updateUiFirstBonusGiven(
                "Extra #$countTeamTwoExtra",
                uiFirstRollScore,
                "Frame #${frame.number}",
                "B2")

        calculateScoresForActiveTeamAndOpponentTeam()
        notifyChange()
    }

    private fun feedReceiverFrameFirstBonusForTeamTwo(frame : Frame){
        val points = uiFirstRollScore.toInt()
        frame.firstBonusReceived.points = points
        frame.firstBonusReceived.providerFrame = null
        frame.firstBonusReceived.providerRollNumber = 0
        frame.firstBonusReceived.extraNumber = countTeamTwoExtra
        frame.firstBonusReceived.message = "Got $points Points from: Extra # $countTeamTwoExtra"
        frame.needsFirstBonus = false
        // update the score of the receiver Frame with the new first bonus value
        frame.score = frame.score + frame.firstBonusReceived.points

        updateUiFirstBonusGiven(
                "Extra #$countTeamTwoExtra",
                uiFirstRollScore,
                "Frame #${frame.number}",
                "B1")

        calculateScoresForActiveTeamAndOpponentTeam()
        notifyChange()
    }

    fun submitEntryWhenFirstRollEqualsTen(view : View){
        // disabled Roll 1 we don't need it anymore
        uiFirstRollEnabled = false
        uiFirstRollFinished = true

        // when the first Roll is Strike then give turn to other team
        uiMessageSubmitButton = "Save this Frame # $uiFrameNumber \ngo to team ${getActiveTeamName()}"
        uiGoToNextTeam = true
        uiSubmitEnabled = true

        // disable Roll 2 because don't need it anymore but show it with its info
        uiHasSecondRollRight = true
        uiSecondRollEnabled = false
        uiSecondRollFinished = true
        uiSecondRollInfo = "<-- not needed"

        // calculate & show result for current score
        uiFrameCompleted = "Rolls completed"
        uiFrameCategory = FrameCategory.STRIKE.categoryName
        uiMessageFirstBonusReceived = "You will have a 1st Bonus !"
        uiMessageSecondBonusReceived = "Also... a 2nd Bonus !"
        uiFrameScore = "10"
        uiFrameTitleInfo= "Frame # $uiFrameNumber ------> Score: $uiFrameScore"

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
        uiMessageSubmitButton = "Enter 2nd Roll for Frame $uiFrameNumber \nand continue the game !"
        uiGoToNextTeam = false
        uiSubmitEnabled = false

        // disactivate Roll 1 we don't need it anymore
        uiFirstRollEnabled = false
        uiFirstRollFinished = true
        uiFirstRollInfo = when(uiFirstRollScore.toInt()) {
            in 0..3 -> "pity... but it's Okay..."
            in 4..6 -> "bofff... acceptable"
            else -> "Good! almost a Strike"
        }

        // show Roll 2
        uiHasSecondRollRight = true
        uiSecondRollEnabled = true
        uiSecondRollFinished = false
        uiSecondRollInfo = "How many Pins you hit ?"

        // calculate & show result after the 1st Roll for current score
        uiFrameCompleted = "Rolls not completed"
        uiFrameCategory = FrameCategory.DEFAULT.categoryName
        uiFrameScore = uiFirstRollScore
        uiFrameTitleInfo= "Frame # $uiFrameNumber ------> Score: $uiFrameScore"

        pageFrame = addFrameObjectToList()
        updateBonuses(pageFrame, 1)
        calculateScoresForActiveTeamAndOpponentTeam()
        closeKeyboard(view)
        notifyChange()
    }

    fun submitEntryWhenSecondRollEqualsOrLessThanRemaining(view : View){
        // keep Roll 2 but disable it
        uiHasSecondRollRight = true
        uiSecondRollEnabled = false
        uiSecondRollFinished = true

        // calculate & show result for current score
        uiFrameCompleted = "Rolls completed"
        uiFrameScore = (uiFirstRollScore.toInt() + uiSecondRollScore.toInt()).toString()
        uiFrameCategory = if (uiFrameScore.toInt() == 10) FrameCategory.SPARE.categoryName else FrameCategory.CLOSED.categoryName
        uiMessageFirstBonusReceived = if (uiFrameScore.toInt() == 10) "You will have a 1st Bonus !" else ""
        uiFrameTitleInfo = "Frame # $uiFrameNumber ------> Score: $uiFrameScore"

        // after the 2nd Rolls is enter and user clicks on Button
        uiMessageSubmitButton = "Save this Frame # $uiFrameNumber \ngo to team ${if (activeTeamNumber == 1) teamTwoName else teamOneName}"
        uiGoToNextTeam = true
        uiSubmitEnabled = true

        updatePageFrame(pageFrame)
        updateBonuses(pageFrame, 2)
        calculateScoresForActiveTeamAndOpponentTeam()
        closeKeyboard(view)
        notifyChange()
    }

    private fun addFrameObjectToList() : Frame{
        var needsFirstBonus : Boolean = false
        var needsSecondBonus : Boolean = false
        when(uiFrameCategory){
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
                uiFrameNumber,
                uiFrameScore.toInt(),
                uiFrameCategory,
                uiFrameCompleted,
                uiFrameTitleInfo,
                uiFirstRollScore.toInt(),
                if (uiSecondRollScore.isEmpty()) 0 else uiSecondRollScore.toInt(),
                if (activeTeamNumber == 1) teamOneName else teamTwoName,
                uiCurrentTeamTotalScore.toInt(),
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
        when(uiFrameCategory){
            FrameCategory.SPARE.categoryName -> needsFirstBonus = true
            else -> {
                needsFirstBonus = false
                needsSecondBonus = false
            }
        }
        pageFrame.score = uiFrameScore.toInt()
        pageFrame.category = uiFrameCategory
        pageFrame.textIsFrameCompleted = uiFrameCompleted
        pageFrame.titleInfo = uiFrameTitleInfo
        pageFrame.secondRollScore = uiSecondRollScore.toInt()
        pageFrame.teamName = if (activeTeamNumber == 1) teamOneName else teamTwoName
        pageFrame.teamTotalScore = uiCurrentTeamTotalScore.toInt()
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
        closeKeyboard(view)
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
        if (teamOneFramesList.size == uiFrameNumber && teamTwoFramesList.size == uiFrameNumber){
            uiFrameNumber++
        }
    }

    private fun didBothTeamsReachedTenFrames() : Boolean{
        return (teamOneFramesList.size == 10 && teamTwoFramesList.size == 10)
    }

    private fun resetModelView(){
        uiFirstRollScore = ""
        uiFirstRollInfo = ""
        uiFirstRollEnabled = true
        uiFirstRollFinished = false

        uiSecondRollScore = ""
        uiSecondRollInfo = ""
        uiSecondRollEnabled = false
        uiHasSecondRollRight = false
        uiSecondRollFinished = false

        uiMessageSubmitButton = "Frame # $uiFrameNumber: Save the 1st Roll \nand continue the game !"
        uiGoToNextTeam =  false
        uiSubmitEnabled = false

        uiFrameTitleInfo = "Frame # $uiFrameNumber ------> Score: 0"
        uiFrameScore = "0"
        uiFrameCategory = FrameCategory.DEFAULT.categoryName
        uiFrameCompleted = ""

        uiMessageFirstBonusReceived = ""
        uiMessageSecondBonusReceived = ""

        resetUiFirstBonusGiven()
        resetUiSecondBonusGiven()

        // update the UI by switching current player and opponent_total_score player score
        calculateScoresForActiveTeamAndOpponentTeam()

        notifyChange()
    }

    private fun calculateScoresForActiveTeamAndOpponentTeam(){
        // by default for team1 we use the ArrayList1 and for team2 we use the ArrayList2
        when(activeTeamNumber){
            // case uiCurrentTeamTotalScore is in ArrayList1 and uiOpponentTeamTotalScore in ArrayList2
            1 -> {
                // calculate the uiCurrentTeamTotalScore
                if(teamOneFramesList.size > 0){
                    uiCurrentTeamTotalScore = getTotalScoreTeamOneFramesList().toString()
                } else {
                    // when it's the first frame and team1 didn't add the Frame to the ArrayList yet because not all of its values are ready except the frame score which we have and we want to display
                    uiCurrentTeamTotalScore = uiFrameScore
                }
                // calculate the uiOpponentTeamTotalScore
                if(teamTwoFramesList.size > 0){
                    uiOpponentTeamTotalScore = getTotalScoreTeamTwoFramesList().toString()
                } else {
                    // when it's the first frame and the opponent_total_score didn't play yet
                    uiOpponentTeamTotalScore = "0"
                }
            }
            // case uiCurrentTeamTotalScore is in ArrayList2 and uiOpponentTeamTotalScore in ArrayList1
            2 -> {
                // calculate the uiCurrentTeamTotalScore
                if(teamTwoFramesList.size > 0){
                    uiCurrentTeamTotalScore = getTotalScoreTeamTwoFramesList().toString()
                } else {
                    // when it's the first frame and team1 didn't add the Frame to the ArrayList yet because not all of its values are ready except the frame score which we have and we want to display
                    uiCurrentTeamTotalScore = uiFrameScore
                }
                // calculate the uiOpponentTeamTotalScore
                if(teamOneFramesList.size > 0){
                    uiOpponentTeamTotalScore = getTotalScoreTeamOneFramesList().toString()
                } else {
                    uiOpponentTeamTotalScore = "0"
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
        val currentPositionInList = teamFramesList.indexOf(providerFrame) // the current Frame is the provider
        var receiverFrame : Frame

        if(teamFramesList.size >= 2 && points != 0){ // should be at least 2 Frames in list
            for (index in 0..currentPositionInList - 1){
                receiverFrame = teamFramesList.get(index)
                if (receiverFrame.needsSecondBonus || receiverFrame.needsFirstBonus){
                    updateReceiverFrame(receiverFrame, points, teamFramesList, providerFrame, providerRollNumber)
                    break
                }
            }
        }
    }

    private fun updateReceiverFrame(receiverFrame : Frame, points : Int, teamFramesList : ArrayList<Frame>, providerFrame : Frame, providerRollNumber : Int){
        // start always by the 2nd Roll that needs to be updated in the case of Strike
        if (receiverFrame.needsSecondBonus){

            updateReceiverFrameSecondBonusReceived(receiverFrame, points, providerFrame, providerRollNumber)

            when(providerRollNumber){
                1 -> {
                    providerFrame.firstBonusGiven = receiverFrame.secondBonusReceived

                    updateUiFirstBonusGiven(
                            "R1",
                            providerFrame.firstRollScore.toString(),
                            "Frame #${receiverFrame.number}",
                            "B2"
                    )
                }
                2 -> {
                    providerFrame.secondBonusGiven = receiverFrame.secondBonusReceived

                    updateUiFirstBonusGiven(
                            "R2",
                            providerFrame.secondRollScore.toString(),
                            "Frame #${receiverFrame.number}",
                            "B2"
                    )
                }
            }
        } else if (receiverFrame.needsFirstBonus){ // this case of Strike OR Spare

            updateReceiverFrameFirstBonusReceived(receiverFrame, points, providerFrame, providerRollNumber)

            when(providerRollNumber){
                1 -> {
                    providerFrame.firstBonusGiven = receiverFrame.firstBonusReceived

                    updateUiSecondBonusGiven(
                            "R1",
                            providerFrame.firstRollScore.toString(),
                            "Frame #${receiverFrame.number}",
                            "B1"
                    )
                }
                2 -> {
                    providerFrame.secondBonusGiven = receiverFrame.firstBonusReceived

                    updateUiSecondBonusGiven(
                            "R2",
                            providerFrame.secondRollScore.toString(),
                            "Frame #${receiverFrame.number}",
                            "B1"
                    )
                }
            }
        }
    }

    private fun calculateProvidedPoints(providerFrame : Frame, providerRollNumber : Int) : Int{
        return if(providerRollNumber == 1) providerFrame.firstRollScore else providerFrame.secondRollScore
    }

    private fun updateUiFirstBonusGiven(
            uiFirstBonusGivenProviderRollOrExtra : String,
            uiFirstBonusGivenPoints : String,
            uiFirstBonusGivenFrameReceiver : String,
            uiFirstBonusGivenBonusReceiver : String){
        this.uiFirstBonusGivenProviderRollOrExtra = uiFirstBonusGivenProviderRollOrExtra
        this.uiFirstBonusGivenPoints = uiFirstBonusGivenPoints
        this.uiFirstBonusGivenFrameReceiver = uiFirstBonusGivenFrameReceiver
        this.uiFirstBonusGivenBonusReceiver = uiFirstBonusGivenBonusReceiver
    }

    private fun updateUiSecondBonusGiven(
            uiSecondBonusGivenProviderRollOrExtra : String,
            uiSecondBonusGivenPoints : String,
            uiSecondBonusGivenFrameReceiver : String,
            uiSecondBonusGivenBonusReceiver : String
    ){
        this.uiSecondBonusGivenProviderRollOrExtra = uiSecondBonusGivenProviderRollOrExtra
        this.uiSecondBonusGivenPoints = uiSecondBonusGivenPoints
        this.uiSecondBonusGivenFrameReceiver = uiSecondBonusGivenFrameReceiver
        this.uiSecondBonusGivenBonusReceiver = uiSecondBonusGivenBonusReceiver
    }

    private fun resetUiFirstBonusGiven(){
        uiFirstBonusGivenProviderRollOrExtra = ""
        uiFirstBonusGivenPoints = ""
        uiFirstBonusGivenFrameReceiver = ""
        uiFirstBonusGivenBonusReceiver = ""
    }

    private fun resetUiSecondBonusGiven(){
        uiSecondBonusGivenProviderRollOrExtra = ""
        uiSecondBonusGivenPoints = ""
        uiSecondBonusGivenFrameReceiver = ""
        uiSecondBonusGivenBonusReceiver = ""
    }

    private fun updateReceiverFrameSecondBonusReceived(receiverFrame : Frame, points : Int, providerFrame : Frame, providerRollNumber : Int){
        receiverFrame.secondBonusReceived.points = points
        receiverFrame.secondBonusReceived.providerFrame = providerFrame
        receiverFrame.secondBonusReceived.receiverFrame = receiverFrame
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
        receiverFrame.firstBonusReceived.receiverFrame = receiverFrame
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
        uiSubmitEnabled = false
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
        uiSubmitEnabled = false
        uiIsButtonReshowAnimationVisible = true
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

                // animation onClick // restart the animation
                containerForFragment!!.setOnClickListener {
                    containerForFragment!!.startAnimation(animationForFragment)
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
                            "Extra  Rolls  phase",
                            Snackbar.LENGTH_LONG).show()
                    countNotifyTeamOneEntersExtra++
                }
            }
            2 -> {
                if(countNotifyTeamTwoEntersExtra == 0) {
                    Snackbar.make(
                            view,
                            "Extra  Rolls  phase",
                            Snackbar.LENGTH_LONG).show()
                    countNotifyTeamTwoEntersExtra++
                }
            }
        }

    }
}