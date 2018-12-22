package bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.view

import android.app.Activity
import android.app.Instrumentation
import android.support.design.widget.TextInputEditText
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.Button
import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.Frame
import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.R
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackingScoreActivityViewTest {

    private lateinit var trackingScoreActivityView : TrackingScoreActivityView
    var teamOneResultFramesList : ArrayList<Frame> = ArrayList()

    // mock didBothTeamsReachedTenFrames to reach 3 Frame
    val sequenceEntriesAndActionsScenarioDev : Array<String> = arrayOf(
            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c"
            )
    // scenario where all the entries of a player are always 10 knocked Pins
    val sequenceEntriesAndActionsScenarioOne : Array<String> = arrayOf(
            // Frame 1
            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            // frame 2
            "10", "c", "c",
            "9", "c",
            "0", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "6", "c",
            "4", "c", "c",

            "10", "c", "c",
            "0", "c",
            "5", "c", "c",

            "10", "c", "c",
            "4", "c",
            "3", "c", "c",

            "10", "c", "c",
            "7", "c",
            "2", "c", "c",

            "10", "c", "c",
            "3", "c",
            "3", "c", "c",

            "10", "c", "c",
            "3", "c",
            "4", "c", "c",

            // all Extras
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c"
            )

    // scenario provided by kotlindatabindingmvvmgamebowlinganimation
    val sequenceEntriesAndActionsScenarioTwo : Array<String> = arrayOf(
            // Frame 1
            "1", "c",
            "4", "c",
            "10", "c", "c",

            // Frame 2
            "4", "c",
            "5", "c",
            "10", "c", "c",

            "6", "c",
            "4", "c",
            "10", "c", "c",

            "5", "c",
            "5", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            "0", "c",
            "1", "c",
            "10", "c", "c",

            "7", "c",
            "3", "c",
            "10", "c", "c",

            "6", "c",
            "4", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            // Frame 10
            "2", "c",
            "8", "c",
            "10", "c", "c",

            // extra
            "6", "c", "c" // extra
    )

    // scenario provided here https://www.youtube.com/watch?v=YgIrYUGiVtc
    val sequenceEntriesAndActionsScenarioThree : Array<String> = arrayOf(
            // Frame 1
            "8", "c",
            "2", "c",
            "10", "c", "c",

            // Frame 2
            "10", "c", "c",
            "10", "c", "c",

            "7", "c",
            "0", "c",
            "10", "c", "c",

            "6", "c",
            "4", "c",
            "10", "c", "c",

            "9", "c",
            "1", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            "8", "c",
            "2", "c",
            "10", "c", "c",

            "2", "c",
            "8", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            // Frame 10
            "9", "c",
            "1", "c",
            "10", "c", "c",

            "10", "c", "c" // extra
    )

    @Rule
    @JvmField
    val activityTestRule : ActivityTestRule<TrackingScoreActivityView> =
            ActivityTestRule<TrackingScoreActivityView>(TrackingScoreActivityView::class.java)

    @Before
    fun setUp() {
        trackingScoreActivityView = activityTestRule.activity
    }

    @Test
    fun testScenario_WhenFirstTeamAlwaysHitTenPinsFromTheFirstRoll(){
        // test it creates 10 Frames
        // test that every Frame will have 2 bonuses received
        // test that every one of the bonuses is 10
        // test that every frame created will have final score of 30
        // test that the final score of the team one is 300
        // test that the number of bonuses is 11

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        Assert.assertNotNull(instrumentation)

        val monitor = instrumentation.addMonitor(
                TrackingScoreActivityView::class.java!!.getName(),
                null,
                false
        )

        Assert.assertNotNull(monitor)

        val entryRollOne = trackingScoreActivityView.findViewById<TextInputEditText>(R.id.first_roll)
        Assert.assertNotNull(entryRollOne)
        val entryRollTwo = trackingScoreActivityView.findViewById<TextInputEditText>(R.id.second_roll)
        Assert.assertNotNull(entryRollTwo)
        val buttonSubmit = trackingScoreActivityView.findViewById<Button>(R.id.bt_to_other_team)
        Assert.assertNotNull(buttonSubmit)

        //entryRollOne.setText("10")

        //clickButtonSubmit(R.id.bt_to_other_team, instrumentation, monitor)
    }

    private fun resetTeamOneFramesList(){
        teamOneResultFramesList = ArrayList()
    }

    private fun enterInRollOne(score : String){

    }

    private fun enterInRollTwo(score : String){

    }

    private fun clickButtonSubmit(resButton : Int, instrumentation : Instrumentation, monitor : Instrumentation.ActivityMonitor){
        val viewMatcher = ViewMatchers.withId(resButton)

        val viewInteraction = Espresso.onView(viewMatcher)

        val viewAction = ViewActions.click()

        viewInteraction.perform(viewAction)

        val trackingScoreActivityView : Activity = instrumentation.waitForMonitorWithTimeout(monitor, 10000)

        Assert.assertNotNull(trackingScoreActivityView)
    }


    @After
    fun tearDown() {
        activityTestRule.finishActivity()
    }
}