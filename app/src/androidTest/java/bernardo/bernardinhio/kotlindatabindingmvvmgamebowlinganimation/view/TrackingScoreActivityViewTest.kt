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



        // start here the input scrore test later
        val sequence = DataProviderForScoreEntries.sequenceEntriesTest1
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