package bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.view

import android.app.Activity
import android.app.Instrumentation
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Assert.*
import org.junit.Test
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.AndroidJUnit4
import android.widget.Button
import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.R
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityViewTest {

    private lateinit var homaActivityView : HomeActivityView

    @Rule
    @JvmField
    val activityTestRule : ActivityTestRule<HomeActivityView>
            = ActivityTestRule<HomeActivityView>(HomeActivityView::class.java)

    @Before
    fun setUp() {
        homaActivityView = activityTestRule.activity
    }

    @Test
    fun whenUserClicksOnStartGameButton_ThenActivityTrackingScoreActivityView_ShouldOpen(){

        val instrumentation : Instrumentation = InstrumentationRegistry.getInstrumentation()
        assertNotNull(instrumentation)

        val monitor : Instrumentation.ActivityMonitor = instrumentation.addMonitor(
                TrackingScoreActivityView::class.java!!.getName(),
                null,
                false
        )

        assertNotNull(monitor)

        val buttonStartGame = homaActivityView.findViewById<Button>(R.id.bt_start_game)

        assertNotNull(buttonStartGame)

        val viewMatcher = ViewMatchers.withId(R.id.bt_start_game)

        val viewInteraction = Espresso.onView(viewMatcher)

        val viewAction = ViewActions.click()

        viewInteraction.perform(viewAction)

        val trackingScoreActivityView : Activity = instrumentation.waitForMonitorWithTimeout(monitor, 10000)

        assertNotNull(trackingScoreActivityView)

        trackingScoreActivityView.finish()
    }

    @After
    fun tearDown() {
        activityTestRule.finishActivity()
    }
}