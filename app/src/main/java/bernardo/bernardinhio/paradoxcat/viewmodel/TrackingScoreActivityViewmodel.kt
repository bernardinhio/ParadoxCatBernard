package bernardo.bernardinhio.paradoxcat.viewmodel

import android.databinding.BaseObservable
import android.graphics.Point
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

        var frameNumber : Int = 1,
        var frameTitleInfo: String = "Frame 1 ------> Score: 0",
        var frameScore : String = "0",
        var frameCategory : String = FrameCategory.DEFAULT.categoryName,
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
                // use later if you need database that deals with the Model
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
                        secondRollInfo = "Ah.. didn't get them all \n...Bad luck !"
                        // after the 2nd Rolls is enter
                        messageSubmitButton = "Save 2nd Roll and calculate \nscore after this Frame $frameNumber"
                        submitEnabled = true
                    }
                    remainingPins -> {
                        secondRollInfo = "Nice SPARE !! \n10 Pins in 2 Rolls!"
                        messageSubmitButton = "Save 2nd Roll and save \nthis Frame $frameNumber"
                        submitEnabled = true
                    }
                    in (remainingPins + 1)..99 -> {
                        secondRollInfo = "can't be above $remainingPins !"
                        messageSubmitButton = "Enter 2nd Roll for Frame $frameNumber \nand continue the game !"
                        submitEnabled = false
                    }
                }
            } else {
                secondRollInfo = "How many Pins you hit?"
                messageSubmitButton = "Enter the 2nd Roll for Frame $frameNumber \nand continue the game !"
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
            switchTeamsAndRestartScoring(view)
        }

        if (didBothTeamsReachedTenFrames()){
            showResultGameAnimation(view)
        }
    }


    fun submitEntryWhenFirstRollEqualsTen(view : View){
        // disabled Roll 1 we don't need it anymore
        firstRollEnabled = false
        firstRollFinished = true

        // when the first Roll is Strike then give turn to other team
        messageSubmitButton = "Save this Frame $frameNumber \nand go to next team ${if (activeTeamNumber == 1) teamTwoName else teamOneName}"
        submitEnabled = true

        // disable Roll 2 because don't need it anymore but show it with its info
        hasSecondRollRight = true
        secondRollEnabled = false
        secondRollFinished = true
        secondRollInfo = "<-- not needed"

        // calculate & show result for current score
        textIsFrameCompleted = "Completed !"
        frameCategory = FrameCategory.STRIKE.categoryName
        messageFirstBonus = "You will have a 1st Bonus !"
        messageSecondBonus = "Also ! a 2nd Bonus !"
        frameScore = "10"
        frameTitleInfo= "Frame $frameNumber ------> Score: $frameScore"

        addCompletedFrameObjectToList()

        checkActiveTeamPreviousFramesIfTheyNeedBonus(firstRoll.toInt(), frameNumber, 1)

        calculateScoresForActiveTeamAndOpponentTeamAfterCheckingIfActiveTeamPreviousFramesNeedBonus()

        closeKeyboard(view)

        notifyChange()
    }



    fun submitEntryWhenFirstRollLessThanTen(view : View){
        messageSubmitButton = "Enter 2nd Roll for Frame $frameNumber \nand continue the game !"
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
        frameCategory = FrameCategory.DEFAULT.categoryName
        frameScore = firstRoll
        frameTitleInfo= "Frame $frameNumber ------> Score: $frameScore"

        checkActiveTeamPreviousFramesIfTheyNeedBonus(firstRoll.toInt(), frameNumber, 1)

        calculateScoresForActiveTeamAndOpponentTeamAfterCheckingIfActiveTeamPreviousFramesNeedBonus()

        closeKeyboard(view)

        notifyChange()
    }



    fun submitEntryWhenSecondRollEqualsOrLessThanRemaining(view : View){
        // keep Roll 2 but disable it
        hasSecondRollRight = true
        secondRollEnabled = false
        secondRollFinished = true

        // calculate & show result for current score
        textIsFrameCompleted = "Completed !"
        frameScore = (firstRoll.toInt() + secondRoll.toInt()).toString()
        frameCategory = if (frameScore.toInt() == 10) FrameCategory.SPARE.categoryName else FrameCategory.CLOSED.categoryName
        messageFirstBonus = if (frameScore.toInt() == 10) "You will have a 1st Bonus !" else ""
        frameTitleInfo = "Frame $frameNumber ------> Score: $frameScore"

        // after the 2nd Rolls is enter and user clicks on Button
        messageSubmitButton = "Save this Frame $frameNumber \nand go to next team ${if (activeTeamNumber == 1) teamTwoName else teamOneName}"
        submitEnabled = true

        addCompletedFrameObjectToList()

        checkActiveTeamPreviousFramesIfTheyNeedBonus(secondRoll.toInt(), frameNumber, 2)

        calculateScoresForActiveTeamAndOpponentTeamAfterCheckingIfActiveTeamPreviousFramesNeedBonus()

        closeKeyboard(view)

        notifyChange()
    }



    private fun addCompletedFrameObjectToList(){
        var needsFirstBonus : Boolean = false
        var needsSecondBonus : Boolean = false
        when(frameCategory){
            FrameCategory.DEFAULT.categoryName -> { }
            FrameCategory.STRIKE.categoryName -> {
                needsFirstBonus = true
                needsSecondBonus = true
            }
            FrameCategory.SPARE.categoryName -> {
                needsFirstBonus = true
            }
        }
        val frame : Frame = Frame(
                frameNumber,
                frameScore.toInt(),
                frameCategory,
                textIsFrameCompleted,
                frameTitleInfo,
                firstRoll.toInt(),
                firstRollInfo,
                if (secondRoll.isEmpty()) 0 else secondRoll.toInt(),
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
    }


    private fun closeKeyboard(view : View){
        (view.context as AppCompatActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun switchTeamsAndRestartScoring(view : View) {
        switchMenuTitle(view)
        switchActiveTeam()
        goToNextFrameIfBothTeamsPlayedSameFrameNumber() // TODO
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
        return (teamOneFramesList.size == 10 && teamTwoFramesList.size == 10)
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

        frameTitleInfo = "Frame $frameNumber ------> Score: 0"
        frameScore = "0"
        frameCategory = FrameCategory.DEFAULT.categoryName
        textIsFrameCompleted = ""

        messageFirstBonus = ""
        messageSecondBonus = ""

        // update the UI by switching current player and opponent player score
        calculateScoresForActiveTeamAndOpponentTeamAfterCheckingIfActiveTeamPreviousFramesNeedBonus()

        notifyChange()
    }





    private fun calculateScoresForActiveTeamAndOpponentTeamAfterCheckingIfActiveTeamPreviousFramesNeedBonus(){
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
                    // when it's the first frame and the opponent didn't play yet
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


    private fun checkActiveTeamPreviousFramesIfTheyNeedBonus(points : Int, frameNumber : Int, fromFrameRoll : Int){
        when(activeTeamNumber){
            1 -> { // activeTeam is team1 and team1 always uses ArrayList1
                updatePreviousFramesBonuses(teamOneFramesList, points, frameNumber, fromFrameRoll)
            }
            2 -> { // activeTeam is team1 and team1 always uses ArrayList1
                updatePreviousFramesBonuses(teamTwoFramesList, points, frameNumber, fromFrameRoll)
            }
        }
    }


    private fun updatePreviousFramesBonuses(teamFramesList : ArrayList<Frame>, points : Int, frameNumber : Int, fromFrameRoll : Int){
        if(teamFramesList.size > 0){
            val currentFrameInList = teamFramesList.last()
            val currentPositionInList = teamFramesList.indexOf(currentFrameInList)
            val previousPositionInList = currentPositionInList - 1
            for (index in previousPositionInList downTo 0){
                val frame : Frame = teamFramesList.get(index)
                if (frame.category == FrameCategory.SPARE.categoryName && frame.needsFirstBonus && points != 0){
                    frame.firstBonus.points = points
                    frame.firstBonus.frameNumber = frameNumber
                    frame.firstBonus.fromFrameRoll = fromFrameRoll
                    frame.firstBonus.bonusMessage = "Got $points from: Frame: $frameNumber Roll: $fromFrameRoll"
                    frame.needsFirstBonus = false
                    frame.score = frame.score + + frame.firstBonus.points
                    break
                } else if (frame.category == FrameCategory.STRIKE.categoryName  && points != 0){
                    if (frame.needsFirstBonus){
                        frame.firstBonus.points = points
                        frame.firstBonus.frameNumber = frameNumber
                        frame.firstBonus.fromFrameRoll = fromFrameRoll
                        frame.firstBonus.bonusMessage = "Got $points from: Frame: $frameNumber Roll: $fromFrameRoll"
                        frame.needsFirstBonus = false
                        frame.score = frame.score + + frame.firstBonus.points
                        break
                    }
                    if (frame.needsSecondBonus){
                        frame.secondBonus.points = points
                        frame.secondBonus.frameNumber = frameNumber
                        frame.secondBonus.fromFrameRoll = fromFrameRoll
                        frame.secondBonus.bonusMessage = "Got $points from: Frame: $frameNumber Roll: $fromFrameRoll"
                        frame.needsSecondBonus = false
                        frame.score = frame.score + + frame.secondBonus.points
                        break
                    }
                }
            }
        } // else do nothing there is no previous Frames & previous bonuses
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

    private fun showResultGameAnimation(view : View){
        submitEnabled = false
        notifyChange()

        inflateFragmentAnimation(view)

        Log.d("ShowAnimation", "Game STOPS after frame $frameNumber")
        Log.d("ShowAnimation", "teamOneFramesList $teamOneFramesList.size")
        Log.d("ShowAnimation", "teamTwoFramesList $teamTwoFramesList.size")
    }



    private fun inflateFragmentAnimation(view : View){
        // In case we want the animation to appear from menu for example before the game finishes
        submitEnabled = false
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

                submitEnabled = false

                // calculate the height of the screen
                val display = trackingScoreActivityView.windowManager.defaultDisplay
                val outSize = Point()
                display.getSize(outSize)
                val heightScreen = outSize.y

                // calculate the offset position of the notification layout when it is created after the menu
                val outLocation = IntArray(2)
                containerForFragment!!.getLocationOnScreen(outLocation)
                val windowsTop = outLocation[1]

                // set Y position at the end of screen
                containerForFragment!!.translationY = (heightScreen - windowsTop - fragment!!.view!!.height).toFloat()

                // setup the Fragment values
                fragment!!.tvCongratulationsMessage!!.text = "It seems someone has won my game! \n\nCongratulations \n${getWinnerTeamName()} \nSCORE ${getWinnerTeamScore()}"
                fragment!!.tvResultsTeamOne!!.text = "$teamOneName    You scored: ${getTotalScoreTeamOneFramesList()}"
                fragment!!.tvResultsTeamTwo!!.text = "$teamTwoName    You scored: ${getTotalScoreTeamTwoFramesList()}"

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
        return if (getTotalScoreTeamOneFramesList() > getTotalScoreTeamTwoFramesList()) teamOneName else teamTwoName
    }




}
